package cz.softinel.uaf.messages;

import java.io.Serializable;
import java.util.Locale;

/**
 * Messages (infos, warnings, errors) for using in application.
 * 
 * @version $Revision: 1.3 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public class Message implements Serializable {

	private static final long serialVersionUID = -3491270268004268239L;

	private String valueOrKey;
	private String value;
	private Object[] parameters;
	private Locale locale;
	private String defaultValue;
	private int countToExpire;

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 */
	public Message(String valueOrKey) {
		this(valueOrKey, null, null, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param defaultValue
	 */
	public Message(String valueOrKey, String defaultValue) {
		this(valueOrKey, null, defaultValue, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param countToExpire
	 */
	public Message(String valueOrKey, int countToExpire) {
		this(valueOrKey, null, null, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param defaultValue
	 * @param countToExpire
	 */
	public Message(String valueOrKey, String defaultValue, int countToExpire) {
		this(valueOrKey, null, defaultValue, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param parameters
	 */
	public Message(String valueOrKey, Object[] parameters) {
		this(valueOrKey, parameters, null, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param parameters
	 * @param defaultValue
	 */
	public Message(String valueOrKey, Object[] parameters, String defaultValue) {
		this(valueOrKey, parameters, defaultValue, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param parameters
	 * @param countToExpire
	 */
	public Message(String valueOrKey, Object[] parameters, int countToExpire) {
		this(valueOrKey, parameters, null, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param parameters
	 * @param defaultValue
	 * @param countToExpire
	 */
	public Message(String valueOrKey, Object[] parameters, String defaultValue, int countToExpire) {
		this(valueOrKey, parameters, defaultValue, Locale.getDefault(), 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param parameters
	 * @param locale
	 */
	public Message(String valueOrKey, Object[] parameters, Locale locale) {
		this(valueOrKey, parameters, null, locale, 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey
	 * @param parameters
	 * @param defaultValue
	 * @param locale
	 */
	public Message(String valueOrKey, Object[] parameters, String defaultValue, Locale locale) {
		this(valueOrKey, parameters, defaultValue, locale, 1);
	}

	/**
	 * Constructor
	 * 
	 * @param valueOrKey key of message all value if not defined in properties and do not have default message
	 * @param parameters parameters of message
	 * @param defaultValue default value of message
	 * @param locale locale of message
	 * @param countToExpire number how much times it should be shown if until it expires
	 */
	public Message(String valueOrKey, Object[] parameters, String defaultValue, Locale locale, int countToExpire) {
		this.valueOrKey = valueOrKey;
		this.parameters = parameters;
		this.locale = locale;
		this.countToExpire = countToExpire;
		this.defaultValue = defaultValue;
		this.value = "";
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the valueOrKey
	 */
	public String getValueOrKey() {
		return valueOrKey;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the countToExpire
	 */
	public int getCountToExpire() {
		return countToExpire;
	}

	/**
	 * @param countToExpire the countToExpire to set
	 */
	public void setCountToExpire(int countToExpire) {
		this.countToExpire = countToExpire;
	}
}