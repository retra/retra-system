package cz.softinel.uaf.util;

public class CommonHelper {

	public static boolean isEquals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		} else {
			return o1.equals(o2);
		}
	}

	public static boolean isNotEquals(Object o1, Object o2) {
		return !isEquals(o1, o2);
	}

}
