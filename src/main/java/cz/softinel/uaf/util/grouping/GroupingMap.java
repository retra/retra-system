package cz.softinel.uaf.util.grouping;

import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.softinel.uaf.util.collections.ReadOnlyMap;

// TODO radek: This is not final solution ... I am waiting for getting experience in using this component
public class GroupingMap {

	private static Logger logger = LoggerFactory.getLogger(GroupingMap.class);

	// Dimension of grouping keys
	private int dimension;
	private int maxMask;

	// Grouped Item storage (by key mask)
	private GroupedItemStorage[] storage;

	@SuppressWarnings("unchecked")
	public GroupingMap(int dimension) {
		// Store dimension
		this.dimension = dimension;
		// Calculate max maxk 2^dimension - 1
		maxMask = 0;
		for (int i = 0; i < dimension; i++) {
			maxMask = maxMask * 2 + 1;
		}
		storage = new GroupedItemStorage[maxMask + 1];
		for (int mask = 0; mask <= maxMask; mask++) {
			storage[mask] = new GroupedItemStorage();
		}
	}

	public void add(GroupingKey key, GroupedItem item) {
		if (key.getDimension() != dimension) {
			throw new IllegalArgumentException(
					"Invalid key dimension " + key.getDimension() + ". Expected " + dimension);
		}
		for (int mask = 0; mask <= maxMask; mask++) {
			GroupingKey groupingKey = GroupingKey.maskKey(key, mask);
			GroupedItem groupedItem = storage[mask].get(groupingKey);
			if (groupedItem == null) {
				groupedItem = new GroupedItem();
				storage[mask].put(groupingKey, groupedItem);
			}
			groupedItem.add(item);
		}
	}

	public int getDimension() {
		return this.dimension;
	}

	public GroupedItem get(GroupingKey key) {
		return storage[key.getMask()].get(key);
	}

	private GroupedItem extraGet(GroupingKey key) {
		return storage[key.getMask()].get(key);
	}

	public GroupedItemStorage getStorageForMask(int mask) {
		return this.storage[mask];
	}

	private class AccessMap extends ReadOnlyMap {
		private final int forDimension;
		private final AccessMap parent;
		private final Object parentKey;

		private AccessMap(AccessMap parent, Object parentKey) {
			if (parent == null) {
				forDimension = 0;
			} else {
				forDimension = parent.getDimension() + 1;
			}
			this.parent = parent;
			this.parentKey = parentKey;
		}

		public int getDimension() {
			return forDimension;
		}

		private void fillParentKeys(LinkedList<Object> keys) {
			if (parent != null) {
				parent.fillParentKeys(keys);
				keys.add(parentKey);
			}
		}

		public Object get(Object key) {
			if (Boolean.FALSE.equals(key)) {
				key = null;
			}
			if (forDimension == dimension - 1) {
				LinkedList<Object> keys = new LinkedList<Object>();
				fillParentKeys(keys);
				keys.add(key);
				logger.debug("JSP access to key: " + keys);
				return extraGet(new GroupingKey(keys.toArray()));
			} else {
				return new AccessMap(this, key);
			}
		}
	}

	public Map getMap() {
		return new AccessMap(null, null);
	}

//	private class DimensionListAccessMap extends ReadOnlyMap {
//		@Override
//		public Object get(Object key) {
//			return getGroupedListForDimension(Integer.valueOf(""+key).intValue()).getList();
//		}
//	}

//	public Map getList() {
//		return new DimensionListAccessMap();
//	}

//	public GroupedList getGroupedListForDimension(int dimension) {
//		GroupedList groupedList = new GroupedList();
//		int mask = 1;
//		for (int i=0; i<dimension; i++) {
//			mask = mask * 2;
//		}
//		for (Entry<GroupingKey, GroupedItem> entry : storage[mask].entrySet()) {
//			Object key = entry.getKey().getKey(dimension);
//			groupedList.getList().add(new GroupedListItem(key, entry.getValue()));
//		}
//		if (storage[0].entrySet().iterator().hasNext()) {
//			groupedList.setSummary(storage[0].entrySet().iterator().next().getValue());
//		} else {
//			groupedList.setSummary(null);
//		}
//		return groupedList;
//	}

	private class ProjectionAccessMap extends ReadOnlyMap {
		@Override
		public Object get(Object key) {
			return getProjection(Integer.valueOf("" + key).intValue());
		}
	}

	// TODO radek: Use some common JSTL access helper
	public ReadOnlyMap getProjectionFor() {
		return new ProjectionAccessMap();
	}

	public Projection getProjection(int dimension) {
		return new Projection(this, dimension);
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		String separator = "";
		for (GroupedItemStorage s : storage) {
			stringBuffer.append(separator);
			stringBuffer.append(s);
			separator = ", ";
		}
		return stringBuffer.toString();
	}

}
