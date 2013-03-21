package cz.softinel.retra.jiraintegration.worklog;

import java.util.Date;

/**
 * Object for converting between {@link JiraWorklog} and the SOAP XML.
 * This class' importance needs to be rethinked.
 */
public class JiraRemoteWorklog {
	
	/**
	 * The ID of the remote worklog in the Jira system.
	 */
	private String id;
	private String author;
	private String comment;
	private String updateAuthor;
	private Date created;
	private Date updated;
	private String timeSpent;
	private long timeSpentInSeconds;
	
	public JiraRemoteWorklog() {
	}
	
	JiraRemoteWorklog(String id, String author, String updateAuthor, Date created, 
			Date updated, String timeSpent, long timeSpentInSeconds) {
		this.id = id;
		this.author = author;
		this.updateAuthor = updateAuthor;
		this.created = created;
		this.updated = updated;
		this.timeSpent = timeSpent;
		this.timeSpentInSeconds = timeSpentInSeconds;
	}

	/**
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the id
	 * @see #id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 * @see #id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the author
	 */
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
	 * @return the updateAuthor
	 */
	public String getUpdateAuthor() {
		return updateAuthor;
	}

	/**
	 * @param updateAuthor the updateAuthor to set
	 */
	public void setUpdateAuthor(String updateAuthor) {
		this.updateAuthor = updateAuthor;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the timeSpent
	 */
	public String getTimeSpent() {
		return timeSpent;
	}

	/**
	 * @param timeSpent the timeSpent to set
	 */
	public void setTimeSpent(String timeSpent) {
		this.timeSpent = timeSpent;
	}

	/**
	 * @return the timeSpentInSeconds
	 */
	public long getTimeSpentInSeconds() {
		return timeSpentInSeconds;
	}

	/**
	 * @param timeSpentInSeconds the timeSpentInSeconds to set
	 */
	public void setTimeSpentInSeconds(long timeSpentInSeconds) {
		this.timeSpentInSeconds = timeSpentInSeconds;
	}
	
	

}
