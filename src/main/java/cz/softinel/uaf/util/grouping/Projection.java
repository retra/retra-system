package cz.softinel.uaf.util.grouping;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Projection {

	private GroupingMap groupingMap;
	private GroupingKey projectionKey;
	private int projectionDimension;
	
	public Projection(GroupingMap groupingMap, int projectionDimension) {
		this.groupingMap = groupingMap;
		this.projectionKey = new GroupingKey(groupingMap.getDimension());
		this.projectionDimension = projectionDimension;
	}
	
	public Projection(ProjectionListItem projectionListItem, int projectionDimension) {
		this.groupingMap = projectionListItem.getProjection().groupingMap;
		// TODO radek: implement it
		this.projectionKey = new GroupingKey(projectionListItem.getGroupingKey());
		this.projectionDimension = projectionDimension;
	}
	
	private class ProjectionListItemComparator implements Comparator<ProjectionListItem> {
		@SuppressWarnings("unchecked")
		public int compare(ProjectionListItem o1, ProjectionListItem o2) {
			Object k1 = o1.getKey();
			Object k2 = o2.getKey();
			Comparable kk1 = null;
			Comparable kk2 = null;
			if (k1 instanceof Comparable && k2 instanceof Comparable) {
				kk1 = (Comparable) k1;
				kk2 = (Comparable) k2;
			} else {
				kk1 = "" + k1;
				kk2 = "" + k2;
			}
			return kk1.compareTo(kk2);
		}
	}
	
	public ProjectionList getList() {
		ProjectionList projectionList = new ProjectionList();
		// Create key for projection mask 
		GroupingKey keyMask = new GroupingKey(projectionKey);
		keyMask.setKey(projectionDimension, "FAKE");
		int mask = keyMask.getMask();
		// Get All values ...
		GroupedItemStorage storage = groupingMap.getStorageForMask(mask);
		for (Map.Entry<GroupingKey, GroupedItem> entry : storage.entrySet()) {
			GroupingKey itemKey = entry.getKey();
			GroupedItem item = entry.getValue();
			if (projectionKey.isSubKey(itemKey)) {
				Object key = itemKey.getKey(projectionDimension);
				Object keyInfo = itemKey.getKeyInfo(projectionDimension);
				ProjectionListItem projectionListItem = new ProjectionListItem(this, itemKey, key, keyInfo, item);
				projectionList.add(projectionListItem);
			}
		}
		ProjectionListItem[] projectionListItems = new ProjectionListItem[projectionList.size()];
		projectionList.toArray(projectionListItems);
		Arrays.sort(projectionListItems, new ProjectionListItemComparator());
		projectionList.clear();
		for (int i=0; i<projectionListItems.length; i++) {
			projectionList.add(projectionListItems[i]);
		}
		return projectionList;
	}
	
	public List<Object> getKeyList() {
		ProjectionList projectionList = getList();
		List<Object> keyList = new LinkedList<Object>();
		for (ProjectionListItem item : projectionList) {
			keyList.add(item.getKey());
		}
		return keyList;
	}
	
	
	@Override
	public String toString() {
		return "Projection: " + getList();
	}
	
	
	/** @deprecated */
	public GroupedItem getSummary() {
		return this.groupingMap.get(this.projectionKey);
	}
	
}
