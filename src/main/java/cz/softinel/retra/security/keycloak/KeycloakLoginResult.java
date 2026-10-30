package cz.softinel.retra.security.keycloak;

import lombok.Data;

/**
 * Represents the result of a Keycloak login attempt.
 */
@Data
public final class KeycloakLoginResult {
	private final boolean ok;
	private final String username;
	private final String email;
	private final String name;
	private final String sub;
	private final String errorStep;
	private final String errorCode;
	private final String errorMessage;
}