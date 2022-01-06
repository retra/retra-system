package cz.softinel.uaf.news;

import javax.xml.bind.annotation.XmlAttribute;

public class SystemNewsItem implements News {

	private String date;
	private String author;
	private String body;
	private String title;

	/**
	 * @return the author
	 */
	@XmlAttribute
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the body
	 */
	@XmlAttribute
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

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
	 * @return the title
	 */
	@XmlAttribute
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsAuthor() {
		return getAuthor();
	}

	public String getNewsBody() {
		return getBody();
	}

	public String getNewsDate() {
		return getDate();
	}

	public String getNewsTitle() {
		return getTitle();
	}

	@Override
	public String toString() {
		return "(SystemNewsItem: date=" + date + ", author=" + author + ", body=" + body + ", title=" + title + ")";
	}


}
