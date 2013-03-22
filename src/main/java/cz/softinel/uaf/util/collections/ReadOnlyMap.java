package cz.softinel.uaf.util.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class ReadOnlyMap<K, V> implements Map<K, V> {

	public abstract V get(Object key);

	public void clear() {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public boolean containsKey(Object key) {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}


	public boolean isEmpty() {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public Set<K> keySet() {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public V put(K key, V value) {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public V remove(Object key) {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public int size() {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

	public Collection<V> values() {
		throw new UnsupportedOperationException("This imethod is not implementd.");
	}

}
