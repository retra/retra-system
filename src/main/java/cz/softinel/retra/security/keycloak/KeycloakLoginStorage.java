package cz.softinel.retra.security.keycloak;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

/**
 * Storage for Keycloak login transactions.
 */
@RequiredArgsConstructor
public class KeycloakLoginStorage {
	private final Map<String, KeycloakLoginTx> storage = new ConcurrentHashMap<>();
	private final KeycloakConfig config;

	public void put(KeycloakLoginTx tx) {
		storage.put(tx.getId(), tx);
	}

	public KeycloakLoginTx get(String id) {
		final KeycloakLoginTx tx = storage.get(id);
		if (tx != null && System.currentTimeMillis() > tx.getExpires()) {
			storage.remove(id);
			return null;
		}
		return tx;
	}
}