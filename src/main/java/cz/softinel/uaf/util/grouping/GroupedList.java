package cz.softinel.uaf.util.grouping;

import java.util.LinkedList;

/**
 * @author Radek Pinc
 * @deprecated
 */
public class GroupedList {

	private LinkedList<GroupedListItem> list;
	private GroupedItem summary;

	public GroupedList() {
		this.list = new LinkedList<GroupedListItem>();
	}

//	private LinkedHashMap<Object, GroupedItem> list = new LinkedHashMap<Object, GroupedItem>();
//
//	public boolean add(Object key, GroupedItem item) {
//		list.put(key, item);
//		return true;
//	}
//
//	public boolean addAll(Collection<? extends GroupedItem> c) {
//		boolean changed = false;
//		for (GroupedItem groupedItem : c) {
//			changed = changed || add(groupedItem);
//		}
//		return changed;
//	}
//
//	public void clear() {
//		list.clear();
//	}
//
//	public boolean contains(Object o) {
//		if (o instanceof GroupedItem) {
//			return list.containsKey(o);
//		} else {
//			return false;
//		}
//	}
//
//	public boolean containsAll(Collection<?> c) {
//		boolean contains = false;
//		for (Object o : c) {
//			contains = contains && contains(o);
//		}
//		return contains;
//	}
//
//	public boolean isEmpty() {
//		return list.isEmpty();
//	}
//
//	// TODO: Create as separate helper class ???
//	private class ValueIterator implements Iterator<GroupedItem> {
//		private final Iterator<Entry<Object, GroupedItem>> iterator;
//		private ValueIterator(Iterator<Entry<Object, GroupedItem>> iterator) {
//			this.iterator = iterator;
//		}
//		public boolean hasNext() {
//			return iterator.hasNext();
//		}
//		public GroupedItem next() {
//			return iterator.next().getValue();
//		}
//		public void remove() {
//			iterator.remove();
//		}
//	}
//	
//	public Iterator<GroupedItem> iterator() {
//		return new ValueIterator(list.entrySet().iterator());
//	}
//
//	public boolean remove(Object o) {
//		if (o instanceof GroupedItem) {
//			return list.remove(o) != null;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean removeAll(Collection<?> c) {
//		boolean changed = false;
//		for (Object o : c) {
//			changed = changed || remove(o);
//		}
//		return changed;
//	}
//
//	public boolean retainAll(Collection<?> c) {
//		// FIXME radek: Implement it ...
//		return false;
//	}
//
//	public int size() {
//		return list.size();
//	}
//
//	public Object[] toArray() {
//		Object[] entries = list.entrySet().toArray();
//		GroupedItem[] groupedItems = new GroupedItem[entries.length];
//		for (int i=0; i<entries.length; i++) {
//			Map.Entry<Object, GroupedItem> entry = (Map.Entry<Object, GroupedItem>) entries[i];
//			groupedItems[i] = entry.getValue();
//		}
//		return groupedItems;
//	}
//
//	public <T> T[] toArray(T[] a) {
//		Object[] entries = list.entrySet().toArray();
//		// TODO: This is not right solution !!!
//		GroupedItem[] groupedItems = (GroupedItem[]) a;
//		for (int i=0; i<entries.length; i++) {
//			Map.Entry<Object, GroupedItem> entry = (Map.Entry<Object, GroupedItem>) entries[i];
//			groupedItems[i] = entry.getValue();
//		}
//		return a;
//	}
//

	public GroupedItem getSummary() {
		return summary;
	}

	public void setSummary(GroupedItem summary) {
		this.summary = summary;
	}

	public LinkedList<GroupedListItem> getList() {
		return list;
	}

}
