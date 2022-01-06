package cz.softinel.retra.core.utils.convertor;

import java.math.BigDecimal;

public class BigDecimalConvertor {

	public static BigDecimal convertToBigDecimalFromString(String value) throws ConvertException {
		BigDecimal result = null;

		try {
			result = new BigDecimal(value);
		} catch (NumberFormatException e) {
			// if couldn't convert -> convert exception
			throw new ConvertException(e);
		}

		return result;
	}

	public static BigDecimal getBigDecimalFromString(String value) {
		BigDecimal result = null;

		try {
			result = convertToBigDecimalFromString(value);
		} catch (ConvertException e) {
			result = null;
		}

		return result;
	}

	public static String convertToStringFromBigDecimal(BigDecimal value) {
		String result = null;

		if (value != null) {
			result = value.toString();
		}

		return result;
	}
}
