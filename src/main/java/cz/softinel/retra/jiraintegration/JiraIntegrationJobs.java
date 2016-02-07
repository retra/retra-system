package cz.softinel.retra.jiraintegration;

import org.apache.log4j.Logger;

import cz.softinel.retra.jiraintegration.soap.JiraSoapClient;

/**
 * Class for grouping the jira integration job methods. This class' {@link #executeJobs()} method is run from
 * a cron job or similar timing tool.
 * @author Erik Szalai
 */
@Deprecated
public class JiraIntegrationJobs {
	private JiraSoapClient jiraSoapClient;
	/**
	 * The boolean to check if Jira integration is enabled.
	 */
	private boolean jiraIntegrationAllowed = false;
	
	/**
	 * @see #jiraIntegrationAllowed
	 */
	public boolean isJiraIntegrationAllowed() {
		return jiraIntegrationAllowed;
	}

	/**
	 * @see #jiraIntegrationAllowed
	 */
	public void setJiraIntegrationAllowed(boolean jiraIntegrationAllowed) {
		this.jiraIntegrationAllowed = jiraIntegrationAllowed;
	}

	public void setJiraSoapClient(JiraSoapClient jiraSoapClient) {
		this.jiraSoapClient = jiraSoapClient;
	}

	/**
	 * Executes the jobs defined by the {@link JiraSoapClient}.
	 */
	public void executeJobs() {
		if(jiraIntegrationAllowed) {
			jiraSoapClient.exportInserted();
			jiraSoapClient.exportUpdated();
			jiraSoapClient.exportWithIssueUpdated();
			jiraSoapClient.exportDeleted();
		}
	}

}
