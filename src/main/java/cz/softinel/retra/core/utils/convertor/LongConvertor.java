package cz.softinel.retra.core.utils.convertor;

public class LongConvertor {
	
	public static Long convertToLongFromString(String value) throws ConvertException {
		Long result = null;
		
		try {
			result = Long.parseLong(value);
		} catch (NumberFormatException e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}

	public static Long getLongFromString(String value) {
		Long result = null;
		
		try {
			result = convertToLongFromString(value);
		}
		catch (ConvertException e) {
			result = null;
		}
		
		return result;
	}
	
	public static String convertToStringFromLong(Long value) {
		String result = null;

		if (value != null) {
			result = value.toString();
		}
		
		return result;
	}
}
