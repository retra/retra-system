package cz.softinel.uaf.util.grouping;

import cz.softinel.uaf.util.CommonHelper;

public class GroupingKey {

	private final Object[] keyParts;
	private final KeyPartStatus[] keyPartStatus;
	
	public GroupingKey(Object[] keyParts, KeyPartStatus[] keyPartStatus) {
		if (keyParts.length != keyPartStatus.length) {
			throw new IllegalArgumentException("Different key dimensions.");
		}
		this.keyParts = keyParts;
		this.keyPartStatus = keyPartStatus;
	}

	private static KeyPartStatus[] makeKeyPartStatus(Object[] keyParts) {
		KeyPartStatus[] keyPartStatus = new KeyPartStatus[keyParts.length];
		for (int i=0; i<keyParts.length; i++) {
			if (keyParts[i] != null) {
				keyPartStatus[i] = KeyPartStatus.DEFINED;
			} else {
				keyPartStatus[i] = KeyPartStatus.NOT_DEFINED;
			}
		}
		return keyPartStatus;
	}
	
	public GroupingKey(Object[] keyParts) {
		this(keyParts, makeKeyPartStatus(keyParts));
	}
	
	private static Object[] makeEmptyKey(int dimension) {
		Object[] keyParts = new Object[dimension];
		for (int i=0; i<dimension; i++) {
			keyParts[i] = null;
		}
		return keyParts;
	}

	public GroupingKey(int dimension) {
		this(makeEmptyKey(dimension));
	}

	public GroupingKey(GroupingKey groupingKey) {
		this.keyParts = new Object[groupingKey.keyParts.length];
		for (int i=0; i<this.keyParts.length; i++) {
			this.keyParts[i] = groupingKey.keyParts[i];
		}
		this.keyPartStatus = new KeyPartStatus[groupingKey.keyPartStatus.length];
		for (int i=0; i<this.keyPartStatus.length; i++) {
			this.keyPartStatus[i] = groupingKey.keyPartStatus[i];
		}
	}
	
//	public static GroupingKey create(Object[] keys) {
//		return new GroupingKey(keys);
//	}
//	
//	public GroupingKey(Object k0) {
//		keys = new Object[] {k0};
//	}
//
//	public GroupingKey(Object k0, Object k1) {
//		keys = new Object[] {k0, k1};
//	}
//
//	public GroupingKey(Object k0, Object k1, Object k2) {
//		keys = new Object[] {k0, k1, k2};
//	}

	public static GroupingKey maskKey(GroupingKey base, int mask) {
		//int originalMask = mask;
		Object[] keys = new Object[base.getDimension()];
		KeyPartStatus[] statuses = new KeyPartStatus[base.getDimension()];
		for (int i=0; i<base.getDimension(); i++) {
			if ((mask&1) == 1) {
				keys[i] = base.keyParts[i];
				statuses[i] = base.keyPartStatus[i];
			} else {
				keys[i] = null;
				statuses[i] = KeyPartStatus.NOT_DEFINED;
			}
			mask = mask / 2;
		}
		GroupingKey groupingKey = new GroupingKey(keys);
		// System.out.println("Masking Key: " + base + " / " + originalMask + " -> " + groupingKey);
		return groupingKey;
	}

	public int getDimension() {
		return keyParts.length;
	}
	
	public Object getKey(int dimension) {
		return keyParts[dimension];
	}
	
	public void setKey(int dimension, Object keyValue) {
		this.keyParts[dimension] = keyValue;
		this.keyPartStatus[dimension] = KeyPartStatus.DEFINED;
	}

	public int getMask() {
		int mask = 0;
		int pointer = 1;
		for (int i=0; i<keyPartStatus.length; i++) {
			if (keyPartStatus[i] == KeyPartStatus.DEFINED) {
				mask = mask | pointer;
			}
			pointer = pointer * 2;
		}
		return mask;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		GroupingKey object = (GroupingKey) obj;
		if (this.keyParts.length != object.keyParts.length) {
			return false;
		}
		if (this.keyPartStatus.length != object.keyPartStatus.length) {
			return false;
		}
		for (int i=0; i<this.keyParts.length; i++) {
			if (! CommonHelper.isEquals(this.keyParts[i], object.keyParts[i])) {
				return false;
			}
			if (this.keyPartStatus[i] != object.keyPartStatus[i]) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = Integer.valueOf(keyParts.length).hashCode();
		for (Object k : keyParts) {
			hashCode = 31 * hashCode + ( k==null ? 0 : k.hashCode() );
		}
		return hashCode;
	}
	
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[");
		String separator = "";
		for (Object k : keyParts) {
			stringBuffer.append(separator);
			stringBuffer.append(k);
			separator = ",";
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	public boolean isSubKey(GroupingKey groupingKey) {
		if (this.keyParts.length != groupingKey.keyParts.length) {
			return false;
		}
		boolean match = true;
		for (int i=0; i<keyParts.length; i++) {
			if (keyPartStatus[i] == KeyPartStatus.DEFINED) {
				if (groupingKey.keyPartStatus[i] != KeyPartStatus.DEFINED) {
					return false;
				}
				if (! CommonHelper.isEquals(keyParts[i], groupingKey.keyParts[i])) {
					return false;
				}
			}
		}
		return match;
	}
}
