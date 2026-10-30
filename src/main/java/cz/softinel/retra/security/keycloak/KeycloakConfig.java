package cz.softinel.retra.security.keycloak;

import lombok.Data;

/**
 * Configuration class for Keycloak OIDC settings.
 */
@Data
public final class KeycloakConfig {
	private final boolean enabled;
	private final String realmUrl;
	private final String clientId;
	private final String clientSecret;
	private final String scopes;
	private final int loginLimitMinutes;
}