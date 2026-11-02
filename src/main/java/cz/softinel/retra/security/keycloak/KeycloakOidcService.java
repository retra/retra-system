package cz.softinel.retra.security.keycloak;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Single-purpose OIDC client for Keycloak (Authorization Code flow + PKCE
 * S256). Replacement for lib-oauth - uses only JDK 11
 * ({@link java.net.http.HttpClient}) and Jackson 1.9.2 (already in the
 * application via jersey-json). No new dependency.
 * 
 * See doc/spec/HBR-5.md.
 */
public class KeycloakOidcService {

	private static final Logger logger = LoggerFactory.getLogger(KeycloakOidcService.class);

	/** Placeholder in the callback URL which is replaced by the login tx id. */
	public static final String LOGIN_TX_ID_PLACEHOLDER = "{loginTxId}";

	private static final int HTTP_TIMEOUT_SECONDS = 10;

	private KeycloakConfig config;

	private final HttpClient httpClient = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(HTTP_TIMEOUT_SECONDS)).build();

	private final ObjectMapper objectMapper = new ObjectMapper();

	// Configuration methods ...

	public void setConfig(KeycloakConfig config) {
		this.config = config;
	}

	public KeycloakConfig getConfig() {
		return config;
	}

	// Business methods ...

	/**
	 * Creates a new login transaction. The <code>{loginTxId}</code> placeholder
	 * in the callback URL is replaced by a fresh transaction id.
	 */
	public KeycloakLoginTx createLoginTx(String callbackUrl) {
		String id = generateRandomString(32);
		long now = System.currentTimeMillis();
		long expires = now + config.getLoginLimitMinutes() * 60L * 1000L;
		return new KeycloakLoginTx(id, generateRandomString(32), generateRandomString(64),
				callbackUrl.replace(LOGIN_TX_ID_PLACEHOLDER, id), now, expires);
	}

	/**
	 * Builds the Keycloak authorization request URI (user agent is redirected
	 * to it).
	 */
	public URI buildAuthorizationRequest(KeycloakLoginTx tx) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("response_type", "code");
		params.put("client_id", config.getClientId());
		params.put("redirect_uri", tx.getCallbackUrl());
		params.put("scope", config.getScopes());
		params.put("state", tx.getState());
		params.put("code_challenge", generateCodeChallenge(tx.getCodeVerifier()));
		params.put("code_challenge_method", "S256");
		return URI.create(config.getAuthEndpoint() + "?" + buildQueryString(params));
	}

	/**
	 * Handles the callback from Keycloak: validates the state, exchanges the
	 * code for tokens (with PKCE verifier) and loads the identity from the
	 * userinfo endpoint.
	 */
	public KeycloakLoginResult handleAuthorizationResponse(KeycloakLoginTx tx, String responseUri) {
		try {
			URI uri = URI.create(responseUri);
			Map<String, String> params = parseQueryString(uri.getRawQuery());

			// validate state (CSRF protection)
			if (tx.getState() == null || !tx.getState().equals(params.get("state"))) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_STATE, null, "Invalid or missing state");
			}

			// Keycloak reported an error
			if (params.containsKey("error")) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_AUTH, params.get("error"),
						params.get("error_description"));
			}

			String code = params.get("code");
			if (code == null || code.length() == 0) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_AUTH, "missing_code",
						"Authorization code is missing");
			}

			// exchange the code for tokens (client authentication: client_secret_basic)
			Map<String, String> tokenResult = exchangeCodeForTokens(code, tx);
			if (tokenResult == null) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_TOKEN, null,
						"Token endpoint returned an error or invalid response");
			}
			String accessToken = tokenResult.get("access_token");
			String idToken = tokenResult.get("id_token");
			if (accessToken == null) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_TOKEN, "missing_access_token",
						"Token response does not contain access_token");
			}

			// optionally check the id_token payload (signature is not verified - the token
			// comes directly from the token endpoint over TLS; identity is confirmed
			// by the direct userinfo call below)
			if (idToken != null) {
				String idTokenError = validateIdTokenPayload(idToken);
				if (idTokenError != null) {
					return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_TOKEN, "invalid_id_token",
							idTokenError);
				}
			}

			// load the identity from userinfo
			Map<String, Object> userinfo = loadUserinfo(accessToken);
			if (userinfo == null) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_UINFO, null,
						"Userinfo endpoint returned an error or invalid response");
			}

			String username = asString(userinfo.get("preferred_username"));
			if (username == null || username.length() == 0) {
				return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_USER, "missing_preferred_username",
						"Userinfo does not contain preferred_username");
			}

			return KeycloakLoginResult.ok(username, asString(userinfo.get("email")), asString(userinfo.get("name")),
					asString(userinfo.get("sub")));
		} catch (Exception e) {
			logger.warn("Keycloak callback processing failed: {}", e.getMessage());
			return KeycloakLoginResult.error(KeycloakLoginResult.ERROR_USER, null, String.valueOf(e.getMessage()));
		}
	}

	// Private methods ...

	/**
	 * POSTs the authorization code to the token endpoint. Returns the parsed
	 * token response or null on error.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> exchangeCodeForTokens(String code, KeycloakLoginTx tx) throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("grant_type", "authorization_code");
		params.put("code", code);
		params.put("redirect_uri", tx.getCallbackUrl());
		params.put("code_verifier", tx.getCodeVerifier());

		String clientAuth = Base64.getEncoder()
				.encodeToString((config.getClientId() + ":" + config.getClientSecret()).getBytes(StandardCharsets.UTF_8));

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(config.getTokenEndpoint()))
				.timeout(Duration.ofSeconds(HTTP_TIMEOUT_SECONDS))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Authorization", "Basic " + clientAuth)
				.POST(HttpRequest.BodyPublishers.ofString(buildQueryString(params))).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			logger.warn("Keycloak token endpoint returned {}: {}", response.statusCode(), response.body());
			return null;
		}
		Map<String, Object> tokenResponse = objectMapper.readValue(response.body(), Map.class);
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Map.Entry<String, Object> entry : tokenResponse.entrySet()) {
			result.put(entry.getKey(), entry.getValue() == null ? null : String.valueOf(entry.getValue()));
		}
		return result;
	}

	/**
	 * GETs the userinfo endpoint with the access token. Returns the parsed
	 * response or null on error.
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> loadUserinfo(String accessToken) throws Exception {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(config.getUserinfoEndpoint()))
				.timeout(Duration.ofSeconds(HTTP_TIMEOUT_SECONDS))
				.header("Authorization", "Bearer " + accessToken).GET().build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			logger.warn("Keycloak userinfo endpoint returned {}: {}", response.statusCode(), response.body());
			return null;
		}
		return objectMapper.readValue(response.body(), Map.class);
	}

	/**
	 * Decodes the payload of the id_token (without signature verification) and
	 * checks iss, aud and exp. Returns an error message or null when ok.
	 */
	@SuppressWarnings("unchecked")
	private String validateIdTokenPayload(String idToken) {
		try {
			String[] parts = idToken.split("\\.");
			if (parts.length < 2) {
				return "Malformed id_token";
			}
			String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
			Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);

			Object iss = payload.get("iss");
			if (iss == null || !config.getRealmUrl().equals(String.valueOf(iss))) {
				return "Invalid iss in id_token: " + iss;
			}
			Object aud = payload.get("aud");
			String audString = aud == null ? null
					: (aud instanceof java.util.List ? String.valueOf(((java.util.List) aud).contains(config.getClientId()))
							: String.valueOf(aud));
			boolean audOk = aud instanceof java.util.List ? ((java.util.List) aud).contains(config.getClientId())
					: config.getClientId().equals(String.valueOf(aud));
			if (!audOk) {
				return "Invalid aud in id_token: " + audString;
			}
			Object exp = payload.get("exp");
			if (exp instanceof Number) {
				long expSeconds = ((Number) exp).longValue();
				if (System.currentTimeMillis() / 1000L > expSeconds) {
					return "Expired id_token";
				}
			}
			return null;
		} catch (Exception e) {
			return "Cannot parse id_token: " + e.getMessage();
		}
	}

	/**
	 * PKCE code challenge: base64url(SHA-256(code_verifier)) without padding.
	 */
	private String generateCodeChallenge(String codeVerifier) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
		} catch (Exception e) {
			throw new RuntimeException("Failed to generate PKCE code challenge", e);
		}
	}

	/**
	 * Random URL-safe string (base64url without padding) from SecureRandom.
	 */
	private String generateRandomString(int length) {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private String buildQueryString(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (sb.length() > 0) {
				sb.append('&');
			}
			sb.append(urlEncode(entry.getKey())).append('=').append(urlEncode(entry.getValue()));
		}
		return sb.toString();
	}

	private Map<String, String> parseQueryString(String query) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		if (query == null || query.length() == 0) {
			return params;
		}
		for (String param : query.split("&")) {
			String[] keyValue = param.split("=", 2);
			String key = urlDecode(keyValue[0]);
			String value = keyValue.length > 1 ? urlDecode(keyValue[1]) : "";
			params.put(key, value);
		}
		return params;
	}

	private String urlEncode(String value) {
		try {
			return URLEncoder.encode(value == null ? "" : value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private String urlDecode(String value) {
		try {
			return java.net.URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static String asString(Object value) {
		return value == null ? null : String.valueOf(value);
	}

}
