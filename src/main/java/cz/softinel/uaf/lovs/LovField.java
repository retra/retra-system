package cz.softinel.uaf.lovs;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class represents one field=value of list of values.
 *
 * @version $Revision: 1.3 $ $Date: 2007-02-08 23:04:12 $
 * @author Petr Sígl
 */
public class LovField {

	private String key;
	private String value;
	private String label;

	/**
	 * @return the key
	 */
	@XmlAttribute
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	@XmlAttribute
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
