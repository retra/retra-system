package cz.softinel.retra.core.utils.helper;

public class BooleanHelper {

	public static boolean isChecked(String strValue) {
		boolean result = false;
		if (strValue != null && ("on".equalsIgnoreCase(strValue))) {
			result = true;
		}
		
		return result;
	}
}
