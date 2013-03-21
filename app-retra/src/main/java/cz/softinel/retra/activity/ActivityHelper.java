package cz.softinel.retra.activity;

public class ActivityHelper {

	public static String getCodeAndName(Activity activity) {
		StringBuffer sb = new StringBuffer();
		sb.append(activity.getCode());
		sb.append(" - ");
		sb.append(activity.getName());
		return sb.toString().trim();
	}

}
