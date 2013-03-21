package cz.softinel.uaf.news;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents all list of values in system.
 *
 * @version $Revision: 1.1 $ $Date: 2007-01-31 18:41:31 $
 * @author Petr Sígl
 */
@XmlRootElement(name="news")
public class SystemNews {
	private List<SystemNewsItem> news = new ArrayList<SystemNewsItem>();

	/**
	 * @return the lovs
	 */
	@XmlElement(name="item")
	public List<SystemNewsItem> getNews() {
		return news;
	}

	/**
	 * @param lovs the lovs to set
	 */
	public void setNews(List<SystemNewsItem> news) {
		this.news = news;
	}
}