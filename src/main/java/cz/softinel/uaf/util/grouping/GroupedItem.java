package cz.softinel.uaf.util.grouping;

public class GroupedItem {

	private long count;
	private float sum;
	private float min;
	private float max;

	public GroupedItem() {
		count = 0;
	}

	public GroupedItem(float number) {
		count = 1;
		sum = min = max = number;
	}

	public void add(float number) {
		if (count == 0) {
			count = 1;
			sum = min = max = number;
		} else {
			count += 1;
			sum += number;
			min = Math.min(min, number);
			max = Math.max(max, number);
		}
	}

	public void add(GroupedItem item) {
		if (count == 0) {
			count = item.count;
			sum = item.sum;
			min = item.min;
			max = item.max;
		} else {
			count += item.count;
			sum += item.sum;
			min = Math.min(min, item.min);
			max = Math.max(max, item.max);
		}
	}

	public long getCount() {
		return count;
	}

	public Float getMax() {
		if (count > 0) {
			return max;
		}
		return null;
	}

	public Float getMin() {
		if (count > 0) {
			return min;
		}
		return null;
	}

	public Float getSum() {
		if (count > 0) {
			return sum;
		}
		return null;
	}

	public Float getAvarage() {
		if (count > 0) {
			return sum / count;
		}
		return null;
	}

	@Override
	public String toString() {
		return "<" + count + "," + sum + ">";
	}

}
