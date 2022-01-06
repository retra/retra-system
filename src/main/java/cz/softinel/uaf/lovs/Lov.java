package cz.softinel.uaf.lovs;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class represents one list of values.
 *
 * @version $Revision: 1.2 $ $Date: 2007-01-30 20:33:36 $
 * @author Petr Sígl
 */
public class Lov {

	private String code;
	private String description;
	private List<LovField> fields;

	/**
	 * @return the code
	 */
	@XmlAttribute
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the description
	 */
	@XmlAttribute
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the fields
	 */
	@XmlElement(name = "field")
	public List<LovField> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<LovField> fields) {
		this.fields = fields;
	}
}
