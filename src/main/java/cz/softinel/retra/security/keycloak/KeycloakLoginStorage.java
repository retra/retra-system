package cz.softinel.retra.security.keycloak;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory storage of Keycloak login transactions.
 * 
 * The application runs on a single node (see doc/spec/HBR-5.md), so a simple
 * map is sufficient. Expired transactions are removed lazily on access.
 */
public class KeycloakLoginStorage {

	private final Map<String, KeycloakLoginTx> storage = new ConcurrentHashMap<String, KeycloakLoginTx>();

	private KeycloakConfig config;

	// Configuration methods ...

	public void setConfig(KeycloakConfig config) {
		this.config = config;
	}

	public KeycloakConfig getConfig() {
		return config;
	}

	// Storage methods ...

	public void put(KeycloakLoginTx tx) {
		cleanupExpired();
		storage.put(tx.getId(), tx);
	}

	/**
	 * Returns the transaction with the given id or null when it does not exist
	 * or is already expired.
	 */
	public KeycloakLoginTx get(String id) {
		if (id == null) {
			return null;
		}
		KeycloakLoginTx tx = storage.get(id);
		if (tx != null && tx.isExpired()) {
			storage.remove(id);
			return null;
		}
		return tx;
	}

	/**
	 * Removes the transaction (used after a finished callback, so that the
	 * callback cannot be replayed).
	 */
	public void remove(String id) {
		if (id != null) {
			storage.remove(id);
		}
	}

	/**
	 * Removes all expired transactions.
	 */
	private void cleanupExpired() {
		for (Iterator<Map.Entry<String, KeycloakLoginTx>> it = storage.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, KeycloakLoginTx> entry = it.next();
			if (entry.getValue().isExpired()) {
				it.remove();
			}
		}
	}

}
