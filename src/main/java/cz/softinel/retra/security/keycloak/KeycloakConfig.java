package cz.softinel.retra.security.keycloak;

/**
 * Configuration for Keycloak OIDC login.
 * 
 * Filled from <code>retra.keycloak.*</code> properties via Spring XML
 * (see <code>WEB-INF/config/securityContext.xml</code>).
 * 
 * Endpoint URLs are derived from <code>realmUrl</code> according to the
 * Keycloak convention (<code>/protocol/openid-connect/...</code>).
 * 
 * See doc/spec/HBR-5.md.
 */
public class KeycloakConfig {

	private boolean enabled;
	private String realmUrl;
	private String clientId;
	private String clientSecret;
	private String scopes;
	private int loginLimitMinutes;

	// Derived endpoints ...

	public String getAuthEndpoint() {
		return getRealmUrl() + "/protocol/openid-connect/auth";
	}

	public String getTokenEndpoint() {
		return getRealmUrl() + "/protocol/openid-connect/token";
	}

	public String getUserinfoEndpoint() {
		return getRealmUrl() + "/protocol/openid-connect/userinfo";
	}

	public String getLogoutEndpoint() {
		return getRealmUrl() + "/protocol/openid-connect/logout";
	}

	// Getters and setters ...

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRealmUrl() {
		return realmUrl;
	}

	public void setRealmUrl(String realmUrl) {
		this.realmUrl = realmUrl;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public int getLoginLimitMinutes() {
		return loginLimitMinutes;
	}

	public void setLoginLimitMinutes(int loginLimitMinutes) {
		this.loginLimitMinutes = loginLimitMinutes;
	}

}
