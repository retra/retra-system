package cz.softinel.retra.type;


/**
 * Helpr methods for type
 *
 * @version $Revision: 1.3 $ $Date: 2007-02-08 20:23:28 $
 * @author Petr Sígl
 */
public class TypeHelper {

	public static String getCodeAndName(Type type) {
		StringBuffer sb = new StringBuffer();
		sb.append(type.getCode());
		sb.append(" - ");
		sb.append(type.getName());
		return sb.toString().trim();
	}

	public static String getCssClass(Type type){
		//TODO: maybe can be cssClass define in Type, than it could return only getType().getCssClass().
		String result = "scheduleDefault";
		if (type != null)	{
			String code = type.getCode();

			//holidays
			if ("HOL".equals(code)){
				result = "scheduleHolidays";
			}
			//internal project
			else if ("IPRJ".equals(code)){
				result = "scheduleInternal";
			}
			//bodyshop
			else if ("BSHP".equals(code)){
				result = "scheduleBodyshop";
			}
			//bussiness trip
			else if ("BTRP".equals(code)){
				result = "scheduleTrip";
			}
		}
		return result;
	}
	
}
