package cz.softinel.retra.security.keycloak;

import lombok.Data;

/**
 * Represents a Keycloak login transaction.
 */
@Data
public final class KeycloakLoginTx {
	private final String id;
	private final String state;
	private final String codeVerifier;
	private final String callbackUrl;
	private final long created;
	private final long expires;
}