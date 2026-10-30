package cz.softinel.retra.security.keycloak;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;

public final class KeycloakOidcService {
	private final KeycloakConfig config;
	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public KeycloakOidcService(final KeycloakConfig config) {
		this.config = config;
	}

	public KeycloakLoginTx createLoginTx(final String callbackUrl) {
		final KeycloakLoginTx tx = new KeycloakLoginTx(
			generateRandomString(32),
			generateRandomString(32),
			generateRandomString(64),
			callbackUrl.replace("{loginTxId}", generateRandomString(32)),
			System.currentTimeMillis(),
			System.currentTimeMillis() + config.getLoginLimitMinutes() * 60 * 1000
		);
		return tx;
	}

	public URI buildAuthorizationRequest(final KeycloakLoginTx tx) {
		final String codeChallenge = generateCodeChallenge(tx.getCodeVerifier());
		final String authEndpoint = config.getRealmUrl() + "/protocol/openid-connect/auth";
		final String request = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s&state=%s&code_challenge=%s&code_challenge_method=S256",
				authEndpoint, config.getClientId(), tx.getCallbackUrl(), config.getScopes(), tx.getState(), codeChallenge);
		return URI.create(request);
	}

	public KeycloakLoginResult handleAuthorizationResponse(final KeycloakLoginTx tx, final String responseUri) {
		final KeycloakLoginResult result = new KeycloakLoginResult(false, null, null, null, null, null, null, null);
		try {
			final URI uri = URI.create(responseUri);
			final Map<String, String> params = parseQueryString(uri.getQuery());

			if (!tx.getState().equals(params.get("state"))) {
				return new KeycloakLoginResult(false, null, null, null, null, "STATE", null, "Invalid state");
			}

			if (params.containsKey("error")) {
				return new KeycloakLoginResult(false, null, null, null, null, "AUTH", params.get("error"), params.get("error_description"));
			}

			final String code = params.get("code");
			final String tokenEndpoint = config.getRealmUrl() + "/protocol/openid-connect/token";
			final String authHeader = "Basic " + Base64.getEncoder().encodeToString((config.getClientId() + ":" + config.getClientSecret()).getBytes());

			final String requestBody = String.format("grant_type=authorization_code&code=%s&redirect_uri=%s&code_verifier=%s",
					code, tx.getCallbackUrl(), tx.getCodeVerifier());

			final HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(tokenEndpoint))
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Authorization", authHeader)
					.POST(HttpRequest.BodyPublishers.ofString(requestBody))
					.build();

			final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				return new KeycloakLoginResult(false, null, null, null, null, "TOKEN", null, "Token endpoint error: " + response.statusCode());
			}

			final Map<String, Object> tokenResponse = objectMapper.readValue(response.body(), Map.class);
			final String accessToken = (String) tokenResponse.get("access_token");
			final String idToken = (String) tokenResponse.get("id_token");

			final String userinfoEndpoint = config.getRealmUrl() + "/protocol/openid-connect/userinfo";
			final HttpRequest userinfoRequest = HttpRequest.newBuilder()
					.uri(URI.create(userinfoEndpoint))
					.header("Authorization", "Bearer " + accessToken)
					.GET()
					.build();

			final HttpResponse<String> userinfoResponse = httpClient.send(userinfoRequest, HttpResponse.BodyHandlers.ofString());
			if (userinfoResponse.statusCode() != 200) {
				return new KeycloakLoginResult(false, null, null, null, null, "UINFO", null, "Userinfo endpoint error: " + userinfoResponse.statusCode());
			}

			final Map<String, Object> userinfo = objectMapper.readValue(userinfoResponse.body(), Map.class);
			return new KeycloakLoginResult(true, (String) userinfo.get("preferred_username"), (String) userinfo.get("email"), (String) userinfo.get("name"), (String) userinfo.get("sub"), null, null, null);
		} catch (final Exception e) {
			return new KeycloakLoginResult(false, null, null, null, null, "USER", null, e.getMessage());
		}
	}

	private String generateRandomString(final int length) {
		final SecureRandom random = new SecureRandom();
		final byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private String generateCodeChallenge(final String codeVerifier) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
		} catch (final Exception e) {
			throw new RuntimeException("Failed to generate code challenge", e);
		}
	}

	private Map<String, String> parseQueryString(final String query) {
		final Map<String, String> params = new HashMap<>();
		if (query != null) {
			for (final String param : query.split("&")) {
				final String[] keyValue = param.split("=", 2);
				params.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
			}
		}
		return params;
	}
}