package cz.softinel.retra.core.utils.convertor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.softinel.retra.core.utils.TypeFormats;

public class DateConvertor {

	private static final Pattern HOUR_FORMAT_PATTERN = Pattern.compile(TypeFormats.HOUR_FORMAT_REGEXP);
	
	public static Date convertToDateFromDateStringHourString(String date, String hour) throws ConvertException {
		Date result = null;
		
		Matcher m = HOUR_FORMAT_PATTERN.matcher(hour);
		if (!m.find()) {
			throw new ConvertException("Invalid hour format.");
		}
		
		String pattern = TypeFormats.DATE_FORMAT + " " + TypeFormats.HOUR_FORMAT;
		String value = date + " " + m.group(1) + ":" + ((m.group(3) == null) ? "00" : m.group(3));
		
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			result = df.parse(value);
		} catch (Exception e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}

	public static Date getDateFromDateStringHourString(String date, String hour) {
		Date result = null;
		
		try {
			result = convertToDateFromDateStringHourString(date, hour);
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}	


	public static Date convertToDateFromDateString(String date)  throws ConvertException {
		Date result = null;
		
		String pattern = TypeFormats.DATE_FORMAT;
		
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			result = df.parse(date);
		} catch (Exception e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}

	public static Date getDateFromDateString(String date) {
		Date result = null;
		
		try {
			result = convertToDateFromDateString(date);
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}	


	public static String convertToDateStringFromDate(Date value) {
		String result = null;
		
		if (value != null) {
			String pattern = TypeFormats.DATE_FORMAT;
			
			DateFormat df = new SimpleDateFormat(pattern);
			result = df.format(value);
		}
		
		return result;
	}
	
	public static String convertToHourStringFromDate (Date value) {
		String result = null;
		
		if (value != null) {
			String pattern = TypeFormats.HOUR_FORMAT;
			
			DateFormat df = new SimpleDateFormat(pattern);
			result = df.format(value);
		}
		
		return result;
	}

	public static String convertToDateHourMinuteStringFromDate(Date value) {
		String result = null;
		
		if (value != null) {
			String pattern = TypeFormats.DATE_FORMAT_WITH_HOURS_MINUTES;
			
			DateFormat df = new SimpleDateFormat(pattern);
			result = df.format(value);
		}
		
		return result;
	}
	
	public static Date convertToDateFromDateHourMinuteSecondString(String date) throws ConvertException {
		Date result = null;
		
		String pattern = TypeFormats.DATE_FORMAT_WITH_HOURS_MINUTES;
		
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			result = df.parse(date);
		} catch (Exception e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}

	public static Date getDateFromDateHourMinuteSecondString(String date) {
		Date result = null;
		
		try {
			result = convertToDateFromDateHourMinuteSecondString(date);
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}

	public static String convertToDateHourMinuteSecondStringFromDate(Date value) {
		String result = null;
		
		if (value != null) {
			String pattern = TypeFormats.DATE_FORMAT_WITH_HOURS_MINUTES;
			
			DateFormat df = new SimpleDateFormat(pattern);
			result = df.format(value);
		}
		
		return result;
	}
	
	public static Date convertToDateFromDateHourMinuteString(String date) throws ConvertException {
		Date result = null;
		
		String pattern = TypeFormats.DATE_FORMAT_WITH_HOURS_MINUTES;
		
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			result = df.parse(date);
		} catch (Exception e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}	

	public static Date getDateFromDateHourMinuteString(String date) {
		Date result = null;
		
		try {
			result = convertToDateFromDateHourMinuteString(date);
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}

	public static String convertToFullDateStringFromDate(Date value) {
		String result = null;
		
		if (value != null) {
			String pattern = TypeFormats.DATE_FORMAT_FULL;
			
			DateFormat df = new SimpleDateFormat(pattern);
			result = df.format(value);
		}
		
		return result;
	}
	
	public static Date convertToDateFromFullDateString(String date) throws ConvertException {
		Date result = null;
		
		String pattern = TypeFormats.DATE_FORMAT_FULL;
		
		DateFormat df = new SimpleDateFormat(pattern);
		try {
			result = df.parse(date);
		} catch (Exception e) {
			//if couldn't convert ->  convert exception
			throw new ConvertException(e);
		}
		
		return result;
	}
	
	public static Date getDateFromFullDateString(String date) {
		Date result = null;
		
		try {
			result = convertToDateFromFullDateString(date);
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}
}
