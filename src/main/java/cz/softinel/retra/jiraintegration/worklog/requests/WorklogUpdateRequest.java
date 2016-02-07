package cz.softinel.retra.jiraintegration.worklog.requests;

import cz.softinel.retra.jiraintegration.worklog.JiraRemoteWorklog;

/**
 * Request to update a worklog in the remote Jira.
 * @author Erik Szalai
 */
@Deprecated
public class WorklogUpdateRequest {
	/**
	 * The login token got from the Jira system.
	 */
	private String loginToken;
	/**
	 * The worklog item in the remote system, presented locally. It must have a remote ID!
	 */
	private JiraRemoteWorklog jiraRemoteWorklog;
	
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
	 * @return
	 * @see #jiraRemoteWorklog
	 */
	public JiraRemoteWorklog getJiraRemoteWorklog() {
		return jiraRemoteWorklog;
	}
	/**
	 * @param jiraRemoteWorklog
	 * @see #jiraRemoteWorklog
	 */
	public void setJiraRemoteWorklog(JiraRemoteWorklog jiraRemoteWorklog) {
		this.jiraRemoteWorklog = jiraRemoteWorklog;
	}

}
