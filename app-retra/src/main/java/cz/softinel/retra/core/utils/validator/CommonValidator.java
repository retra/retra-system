package cz.softinel.retra.core.utils.validator;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import cz.softinel.retra.core.utils.TypeFormats;
import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;

public class CommonValidator {
	
	private static final Pattern HOUR_FORMAT_PATTERN = Pattern.compile(TypeFormats.HOUR_FORMAT_REGEXP);
	
	private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile(TypeFormats.DATE_FORMAT_REGEXP);

	public static boolean isValidDate(String value){
		if (StringUtils.hasText(value)) {
			Matcher m = DATE_FORMAT_PATTERN.matcher(value);
			if (m.find()){
				// since Java validation accepts strange dates (like 33.33.3333)
				// we have to check range for month and day section
				int firstDotIndex = value.indexOf(".");
				int lastDotIndex = value.lastIndexOf(".");
				String dayPart = value.substring(0, firstDotIndex);
				String monthPart = value.substring(firstDotIndex + 1, lastDotIndex);
				if (isDayInRange(dayPart) && isMonthInRange(monthPart)) {
					try {
						DateConvertor.convertToDateFromDateString(value);
					} catch (ConvertException ce) {
						return false;
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean isValidEmail(String email){
		boolean result = false;
		if(StringUtils.hasText(email)){
			result = email.matches(TypeFormats.EMAIL_REGEXP);
		}
		return result;
	}
	
	public static void validateEmail(String fieldName, Errors errors, String resourceKey, String defaultMessage){
		String email = (String) errors.getFieldValue(fieldName);
		boolean valid = isValidEmail(email);
		if(!valid){
			if(defaultMessage == null){
				errors.rejectValue(fieldName, resourceKey);
			}else{
				errors.rejectValue(fieldName, resourceKey, defaultMessage);
			}
		}
	}
	
	public static void validateDate(String fieldName, Errors errors, String resourceKey, String defaultMessage){
		String date = (String)errors.getFieldValue(fieldName);
		
		if (StringUtils.hasText(date)) {
			boolean valid = CommonValidator.isValidDate(date);
			if (!valid) {
				if (defaultMessage == null) {
					errors.rejectValue(fieldName, resourceKey);
				}
				else {
					errors.rejectValue(fieldName, resourceKey, defaultMessage);					
				}
			}
		}
	}
	
	private static boolean isInRange(String str, int lowerRange, int higherRange){
		try{
			int val = Integer.parseInt(str);
			return val >= lowerRange && val <= higherRange;
		}catch(Exception exc){
			return false;
		}
	}
	
	private static boolean isDayInRange(String day){
		return isInRange(day, 1, 31);
	}
	
	private static boolean isMonthInRange(String month){
		return isInRange(month, 1, 12);
	}
	
	private static boolean isHourInRange(String hourStr, String minuteStr){
		if (isInRange(minuteStr, 0, 0)) {
			return isInRange(hourStr, 0, 24);
		} else {
			return isInRange(hourStr, 0, 23);
		}
	}
	
	private static boolean isMinuteInRange(String minuteStr){
		return isInRange(minuteStr, 0, 59);
	}

//	public static class HourStructure {
//		private final int hour;
//		private final int minute;
//		public HourStructure(String hour) {
//		}
//		public int getHour() {
//			return hour;
//		}
//		public int getMinute() {
//			return minute;
//		}
//		public static HourStructure parse(String hour) {
//			hour = hour.replaceAll("[^0-9]", "");
//			if (hour.length() <= 2) {
//				this.hour = Integer.valueOf(hour);
//				this.minute = 0;
//			} else {
//				this.hour = Integer.valueOf(hour.substring(0, hour.length()-2));
//				this.minute = Integer.valueOf(hour.substring(hour.length()-2, hour.length()));
//			}
//		}
//	}
	
	public static boolean isValidHour(String value){
		if (StringUtils.hasText(value)) {
			Matcher m = HOUR_FORMAT_PATTERN.matcher(value);
			if (m.find()){
				String hourPart = m.group(1);
				String minutePart = m.group(3);
				if (minutePart == null) {
					minutePart = "00";
				}
				if (isHourInRange(hourPart, minutePart) && isMinuteInRange(minutePart)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public static void validateHour(String fieldName, Errors errors, String resourceKey, String defaultMessage){
		String hour = (String)errors.getFieldValue(fieldName);

		if (StringUtils.hasText(hour)) {
			boolean valid = CommonValidator.isValidHour(hour);
			if (!valid) {
				if (defaultMessage == null) {
					errors.rejectValue(fieldName, resourceKey);
				}
				else {
					errors.rejectValue(fieldName, resourceKey, defaultMessage);
				}
			}
		}
	}

	public static boolean isFirstGreaterThanSecond(String keyFirst, String keySecond, Errors errors){
		int resultCompare = compareTwoDates(keyFirst, keySecond, errors);
		
		if (resultCompare > 0){
			return true;
		}
		
		return false;
	}

	public static boolean isFirstGreaterOrEqualThanSecond(String keyFirst, String keySecond, Errors errors){
		int resultCompare = compareTwoDates(keyFirst, keySecond, errors);

		
		if (resultCompare >= 0){
			return true;
		}
		
		return false;
	}

	public static boolean isFirstLessThanSecond(String keyFirst, String keySecond, Errors errors){
		int resultCompare = compareTwoDates(keyFirst, keySecond, errors);

		if (resultCompare < 0){
			return true;
		}
		
		return false;
	}

	public static boolean isFirstLessOrEqualThanSecond(String keyFirst, String keySecond, Errors errors){
		int resultCompare = compareTwoDates(keyFirst, keySecond, errors);

		if (resultCompare <= 0){
			return true;
		}
		
		return false;
	}

	public static boolean isFirstEqualSecond(String keyFirst, String keySecond, Errors errors){
		int resultCompare = compareTwoDates(keyFirst, keySecond, errors);
	
		if (resultCompare == 0){
			return true;
		}
		
		return false;
	}

	public static void validateBigDecimal(String fieldName, Errors errors, String resourceKey, String defaultMessage){
		String bd = (String)errors.getFieldValue(fieldName);

		if (StringUtils.hasText(bd)) {
			boolean valid = CommonValidator.isValidBigDecimal(bd);
			if (!valid) {
				if (defaultMessage == null) {
					errors.rejectValue(fieldName, resourceKey);
				}
				else {
					errors.rejectValue(fieldName, resourceKey, defaultMessage);
				}
			}
		}
	}

	public static boolean isValidBigDecimal(String value){
		if (StringUtils.hasText(value)) {
			Matcher m = HOUR_FORMAT_PATTERN.matcher(value);
			if (m.find()){
				String hourPart = m.group(1);
				String minutePart = m.group(3);
				if (minutePart == null) {
					minutePart = "00";
				}
				if (isHourInRange(hourPart, minutePart) && isMinuteInRange(minutePart)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	private static int compareTwoDates(String keyFirst, String keySecond, Errors errors) {
		String firstStr = (String)errors.getFieldValue(keyFirst);
		String secondStr = (String)errors.getFieldValue(keySecond);
		
		if (CommonValidator.isValidDate(firstStr) && CommonValidator.isValidDate(secondStr)){
			Date first = null;
			Date second = null;
			try {
				first = DateConvertor.convertToDateFromDateString(firstStr);
				second = DateConvertor.convertToDateFromDateString(secondStr);
			}
			catch (ConvertException e) {
				throw new ValidationException("Couldn't convert -> Validate Exception."); 
			}
			
			return first.compareTo(second);
		}
		
		//not valid date, couldn't compare
		throw new ValidationException("Couldn't compare dates, because of invalid one of them or both -> Validate Exception."); 

	}

}
