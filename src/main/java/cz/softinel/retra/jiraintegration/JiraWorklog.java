package cz.softinel.retra.jiraintegration;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraWorklog implements Serializable {

	private String id;
	private String timeSpentSeconds;
	private String started;
	private String comment;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Author implements Serializable {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	private Author author;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimeSpentSeconds() {
		return timeSpentSeconds;
	}

	public void setTimeSpentSeconds(String timeSpentSeconds) {
		this.timeSpentSeconds = timeSpentSeconds;
	}

	public String getStarted() {
		return started;
	}

	public void setStarted(String started) {
		this.started = started;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

}
