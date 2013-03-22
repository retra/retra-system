package cz.softinel.uaf.holidays;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents all list of holidays in system.
 *
 * @version $Revision: 1.1 $ $Date: 2007-01-31 07:37:39 $
 * @author Petr Sígl
 */
@XmlRootElement(name="holidays")
public class Holidays {

	private List<Item> items;
	private String country;

	/**
	 * @return the country
	 */
	@XmlAttribute
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the items
	 */
	@XmlElement(name="item")
	public List<Item> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}
}