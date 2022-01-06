package cz.softinel.retra.jiraintegration.worklog.requests;

import cz.softinel.retra.jiraintegration.worklog.JiraRemoteWorklog;

/**
 * Add a worklog to the given issue. This request is the object for the
 * marshaller.
 * 
 * @author Erik Szalai
 */
@Deprecated
public class WorklogAddRequest {

	/**
	 * The token after the login.
	 */
	private String loginToken;
	/**
	 * The details of the worklog.
	 */
	private JiraRemoteWorklog jiraRemoteWorklog;
	/**
	 * The jira issue, to which to attach the worklog.
	 */
	private String jiraIssue;

	/**
	 * @return the jiraRemoteWorklog
	 * @see #jiraRemoteWorklog
	 */
	public JiraRemoteWorklog getJiraRemoteWorklog() {
		return jiraRemoteWorklog;
	}

	/**
	 * @param jiraRemoteWorklog the jiraRemoteWorklog to set
	 * @see #jiraRemoteWorklog
	 */
	public void setJiraRemoteWorklog(JiraRemoteWorklog jiraRemoteWorklog) {
		this.jiraRemoteWorklog = jiraRemoteWorklog;
	}

	/**
	 * @return
	 * @see #loginToken
	 */
	public String getLoginToken() {
		return loginToken;
	}

	/**
	 * @param loginToken
	 * @see #loginToken
	 */
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	/**
	 * @return the jiraIssue
	 * @see WorklogAddRequest#jiraIssue
	 */
	public String getJiraIssue() {
		return jiraIssue;
	}

	/**
	 * @param jiraIssue the jiraIssue to set
	 * @see {@link #jiraIssue}
	 */
	public void setJiraIssue(String jiraIssue) {
		this.jiraIssue = jiraIssue;
	}

}
