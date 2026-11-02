package cz.softinel.retra.security.keycloak;

/**
 * One Keycloak login transaction (authorization code flow with PKCE).
 * 
 * Holds the values that must survive the redirect to Keycloak and back:
 * the transaction id, the <code>state</code> parameter, the PKCE
 * <code>code_verifier</code> and the callback URL (with the transaction id
 * already filled in).
 * 
 * See doc/spec/HBR-5.md.
 */
public class KeycloakLoginTx {

	private final String id;
	private final String state;
	private final String codeVerifier;
	private final String callbackUrl;
	private final long created;
	private final long expires;

	public KeycloakLoginTx(String id, String state, String codeVerifier, String callbackUrl, long created,
			long expires) {
		this.id = id;
		this.state = state;
		this.codeVerifier = codeVerifier;
		this.callbackUrl = callbackUrl;
		this.created = created;
		this.expires = expires;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > expires;
	}

	// Getters ...

	public String getId() {
		return id;
	}

	public String getState() {
		return state;
	}

	public String getCodeVerifier() {
		return codeVerifier;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public long getCreated() {
		return created;
	}

	public long getExpires() {
		return expires;
	}

}
