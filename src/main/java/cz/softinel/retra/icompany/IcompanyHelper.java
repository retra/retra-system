package cz.softinel.retra.icompany;

public class IcompanyHelper {

	public static String getCodeAndName(Icompany icompany) {
		StringBuffer sb = new StringBuffer();
		sb.append(icompany.getCode());
		sb.append(" - ");
		sb.append(icompany.getName());
		return sb.toString().trim();
	}

}
