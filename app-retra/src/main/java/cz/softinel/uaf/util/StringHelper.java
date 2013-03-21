package cz.softinel.uaf.util;

public class StringHelper {

	public static boolean isEmpty(final String string) {
		if (string == null) {
			return true;
		}
		return string.trim().length() == 0;
	}
	
	public static boolean notEmpty(final String string) {
		return ! isEmpty(string);
	}
	
}
