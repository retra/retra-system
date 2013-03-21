package cz.softinel.uaf.holidays;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class represents one field=value of list of values.
 * 
 * @version $Revision: 1.1 $ $Date: 2007-01-31 07:37:39 $
 * @author Petr Sígl
 */
public class Term {

	private String date;
	private String validFrom;
	private String validTo;

	/**
	 * @return the date
	 */
	@XmlAttribute
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the validFrom
	 */
	@XmlAttribute
	public String getValidFrom() {
		return validFrom;
	}

	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * @return the validTo
	 */
	@XmlAttribute
	public String getValidTo() {
		return validTo;
	}

	/**
	 * @param validTo the validTo to set
	 */
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
}
