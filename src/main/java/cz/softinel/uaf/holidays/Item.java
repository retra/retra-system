package cz.softinel.uaf.holidays;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class represents one field=value of list of values.
 * 
 * @version $Revision: 1.1 $ $Date: 2007-01-31 07:37:39 $
 * @author Petr Sígl
 */
public class Item {

	private String key;
	private String name;
	private List<Term> terms;

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
	 * @return the name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the terms
	 */
	@XmlElement(name = "term")
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * @param terms the terms to set
	 */
	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

}
