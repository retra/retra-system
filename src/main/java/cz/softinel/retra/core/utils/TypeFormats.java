package cz.softinel.retra.core.utils;

public final class TypeFormats {

	public static final String ATTRIBUTE_NAME_TYPE_FORMATS = "TypeFormats";
	
	public static final String DATE_FORMAT = "dd.MM.yyyy";
	public static final String DATE_FORMAT_DOW = "dd.MM.yyyy - EEE";
	public static final String DATE_FORMAT_REGEXP = "^[0-9]{0,1}[0-9]{1}\\.[0-9]{0,1}[0-9]{1}\\.[0-9]{4}$";

	public static final String DATE_FORMAT_WITH_HOURS_MINUTES = "dd.MM.yyyy HH:mm";
	public static final String DATE_FORMAT_WITH_HOURS_MINUTES_SECONDS = "dd.MM.yyyy HH:mm:ss";
	public static final String DATE_FORMAT_FULL = "dd.MM.yyyy HH:mm:ss.SSS";

	public static final String HOUR_FORMAT = "HH:mm";
	public static final String HOUR_FORMAT_REGEXP = "^([0-9]{1,2})([: ])?([0-9]{2})?$";
	public static final String HOURS_FORMAT = "##0.00";

	public static final String EMAIL_REGEXP = "^.{2,}@.{2,}\\.[a-zA-Z]{2,4}$";
	
	private static TypeFormats singleton = new TypeFormats();
	
	private TypeFormats() {
		//private constructor
	}

	/**
	 * To get instance for type formats
	 * 
	 * @return
	 */
	public static TypeFormats getInstance() {
		return singleton;
	}

	/**
	 * @return the dATE_FORMAT
	 */
	public String getDATE_FORMAT() {
		return DATE_FORMAT;
	}

	/**
	 * @return the DATE_FORMAT_DOW
	 */
	public String getDATE_FORMAT_DOW() {
		return DATE_FORMAT_DOW;
	}
	
	/**
	 * @return the hOUR_FORMAT
	 */
	public String getHOUR_FORMAT() {
		return HOUR_FORMAT;
	}

	/**
	 * @return the dATE_FORMAT_FULL
	 */
	public String getDATE_FORMAT_FULL() {
		return DATE_FORMAT_FULL;
	}

	/**
	 * @return the dATE_FORMAT_WITH_HOURS_MINUTES
	 */
	public String getDATE_FORMAT_WITH_HOURS_MINUTES() {
		return DATE_FORMAT_WITH_HOURS_MINUTES;
	}

	/**
	 * @return the dATE_FORMAT_WITH_HOURS_MINUTES_SECONDS
	 */
	public String getDATE_FORMAT_WITH_HOURS_MINUTES_SECONDS() {
		return DATE_FORMAT_WITH_HOURS_MINUTES_SECONDS;
	}
}
