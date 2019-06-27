package cz.softinel.uaf.util.grouping;

import cz.softinel.uaf.util.collections.ReadOnlyMap;

public class ProjectionListItem {

	private Projection projection;
	private GroupingKey groupingKey;
	private Object key;
	private GroupedItem value;
	private Object keyInfo;
	
	public ProjectionListItem(Projection projection, GroupingKey groupingKey, Object key, Object keyInfo, GroupedItem value) {
		this.projection = projection;
		this.groupingKey = groupingKey;
		this.key = key;
		this.keyInfo = keyInfo;
		this.value = value;
	}
	
	public Projection getProjection() {
		return projection;
	}

	public GroupingKey getGroupingKey() {
		return groupingKey;
	}

	public Object getKey() {
		return key;
	}
	
	public GroupedItem getValue() {
		return value;
	}

	public Object getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(Object keyInfo) {
		this.keyInfo = keyInfo;
	}

	private class ProjectionAccessMap extends ReadOnlyMap {
		@Override
		public Object get(Object key) {
			return getProjection(Integer.valueOf(""+key).intValue());
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
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(key);
		sb.append("-");
		sb.append(value);
		sb.append(")");
		return sb.toString();
	}
}
