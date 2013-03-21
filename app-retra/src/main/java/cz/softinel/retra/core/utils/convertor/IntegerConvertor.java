package cz.softinel.retra.core.utils.convertor;

public class IntegerConvertor {
	
	public static Integer convertToIntegerFromString(String value) throws ConvertException {
		Integer result = null;
		
		try {
			result = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}

	public static Integer getIntegerFromString(String value) {
		Integer result = null;
		
		try {
			result = convertToIntegerFromString(value);
		}
		catch (ConvertException e) {
			result = null;
		}
		
		return result;
	}
	
	public static String convertToStringFromInteger(Integer value) {
		String result = null;

		if (value != null) {
			result = value.toString();
		}
		
		return result;
	}
}
