/**
 * 
 */
package cz.softinel.uaf.util.grouping;

/**
 * @author Radek Pinc
 * @deprecated
 */
public class GroupedListItem {
	private Object key;
	private GroupedItem value;

	public GroupedListItem(Object key, GroupedItem value) {
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public GroupedItem getValue() {
		return value;
	}
}