package cz.softinel.retra.category;

public class CategoryHelper {

	public static String getCodeAndName(Category category) {
		StringBuffer sb = new StringBuffer();
		sb.append(category.getCode());
		sb.append(" - ");
		sb.append(category.getName());
		return sb.toString().trim();
	}

}
