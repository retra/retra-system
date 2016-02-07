package cz.softinel.retra.jiraintegration.soap;

import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;

/**
 * Interface for the web service messaging class.
 * <p>Known implementation: {@link JiraSoapClientImpl}
 * @author Erik Szalai
 * @see JiraSoapClientMarshaller
 * @see JiraWorklog
 */
@Deprecated
public interface JiraSoapClient {
	
	/**
	 * Export all the newly inserted JiraWorklogs (new or deleted-recreated).
	 * @see JiraWorklog#CREATED
	 */
	void exportInserted();
	
	/**
	 * Export the updated JiraWorklogs. Updated where the details but not the Jira issue field.
	 * @see JiraWorklog#UPDATED
	 */
	void exportUpdated();
	
	/**
	 * Delete all the JiraWorklogs where the Worklog was deleted (the {@link JiraWorklog#getWorklog()} returns null).
	 */
	void exportDeleted();
	
	/**
	 * Export the JiraWorklogs, where the jira issue field was updated.
	 * @see JiraWorklog#ISSUE_UPDATED
	 */
	void exportWithIssueUpdated();
}
