package cz.softinel.retra.icompany;

public class IcompanyHelper {

	public static String getCodeAndName(Icompany icompany) {
		StringBuffer sb = new StringBuffer();
		if (icompany.getCode() != null) {
			sb.append(icompany.getCode());
			if (icompany.getName() != null) {
				sb.append(" - ");
			}
		}
		if (icompany.getName() != null) {
			sb.append(icompany.getName());			
		}
		return sb.toString().trim();
	}

}
