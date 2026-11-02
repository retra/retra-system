package cz.softinel.retra.security.keycloak;

/**
 * Result of the Keycloak authorization callback processing.
 * 
 * When <code>ok</code> is true, <code>username</code> contains the
 * <code>preferred_username</code> from Keycloak (mapped to
 * <code>sis11login.sis11ldaplogin</code>), together with email, name and sub.
 * Otherwise <code>errorStep</code> describes the failed step (STATE, AUTH,
 * TOKEN, UINFO, USER) and <code>errorCode</code>/<code>errorMessage</code>
 * carry details.
 * 
 * See doc/spec/HBR-5.md.
 */
public class KeycloakLoginResult {

	/** Error step - invalid or expired state / transaction. */
	public static final String ERROR_STATE = "STATE";
	/** Error step - Keycloak returned an error in the callback. */
	public static final String ERROR_AUTH = "AUTH";
	/** Error step - token endpoint failed. */
	public static final String ERROR_TOKEN = "TOKEN";
	/** Error step - userinfo endpoint failed. */
	public static final String ERROR_UINFO = "UINFO";
	/** Error step - user could not be resolved / unexpected failure. */
	public static final String ERROR_USER = "USER";

	private final boolean ok;
	private final String username;
	private final String email;
	private final String name;
	private final String sub;
	private final String errorStep;
	private final String errorCode;
	private final String errorMessage;

	public KeycloakLoginResult(boolean ok, String username, String email, String name, String sub,
			String errorStep, String errorCode, String errorMessage) {
		this.ok = ok;
		this.username = username;
		this.email = email;
		this.name = name;
		this.sub = sub;
		this.errorStep = errorStep;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public static KeycloakLoginResult ok(String username, String email, String name, String sub) {
		return new KeycloakLoginResult(true, username, email, name, sub, null, null, null);
	}

	public static KeycloakLoginResult error(String errorStep, String errorCode, String errorMessage) {
		return new KeycloakLoginResult(false, null, null, null, null, errorStep, errorCode, errorMessage);
	}

	// Getters ...

	public boolean isOk() {
		return ok;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getSub() {
		return sub;
	}

	public String getErrorStep() {
		return errorStep;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
