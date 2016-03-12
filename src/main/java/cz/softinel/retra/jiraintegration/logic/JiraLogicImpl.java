package cz.softinel.retra.jiraintegration.logic;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.softinel.retra.jiraintegration.JiraConfig;
import cz.softinel.retra.jiraintegration.JiraConnector;
import cz.softinel.retra.jiraintegration.JiraHelper;
import cz.softinel.retra.jiraintegration.JiraIssue;
import cz.softinel.retra.worklog.Worklog;

public class JiraLogicImpl implements JiraLogic {

	protected Log logger = LogFactory.getLog(this.getClass());
	
	private JiraConfig jiraConfig;
	private JiraConnector jiraConnector;

	public JiraConfig getJiraConfig() {
		return jiraConfig;
	}

	public void setJiraConfig(JiraConfig jiraConfig) {
		this.jiraConfig = jiraConfig;
	}

	public JiraConnector getJiraConnector() {
		return jiraConnector;
	}

	public void setJiraConnector(JiraConnector jiraConnector) {
		this.jiraConnector = jiraConnector;
	}

	public List<JiraIssue> findJiraIssuesForUser(String ldapLogin) {
		List<JiraIssue> result = null;
		if (isJiraEnabled()) {
			result = jiraConnector.findIssuesForWorklog(ldapLogin);
			//add issues to cache
			if (jiraConfig.getJiraCache() != null && result != null && !result.isEmpty()) {
				for (JiraIssue issue : result) {
					jiraConfig.getJiraCache().addIssueToCache(issue);
				}
			}
		}
		return result;
	}

	public JiraIssue getJiraIssue(final String code) {
		JiraIssue result = null;
		
		//first try cache
		if (jiraConfig.getJiraCache() != null) {
			result = jiraConfig.getJiraCache().getIssueFromCache(code);
		}
		
		//second try rest-api
		if (result == null) {
			result = getJiraIssueFromConnector(code);
		}
		
		return result;
	}
	
	public void addJiraWorklog(Worklog worklog) {
		if (isJiraEnabled()) {
			List<String> issues = JiraHelper.findIssueCodesInText(worklog.getDescription());
			if (issues != null && !issues.isEmpty()) {
				final String issueKey = issues.get(0);
				final Date started = worklog.getWorkFrom();
				final long duration = worklog.getSeconds(); 
				final String loginName = worklog.getEmployee().getUser().getLogin().getLdapLogin();
				final String comment = worklog.getDescription();
				
				if (!jiraConnector.addWorklog(issueKey, started, duration, loginName, comment)) {
					logger.error("Couldn't log in JIRA.");
				} else {
					getJiraIssueFromConnector(issueKey);
				}
				
			}
		}
	}
	
	@Override
	public void updateJiraWorklog(Worklog oldWorklog, Worklog newWorklog) {
		if (isJiraEnabled()) {
			String id = null;
			List<String> issues = JiraHelper.findIssueCodesInText(oldWorklog.getDescription());
			String oldIssueKey = "old";
			if (issues != null && !issues.isEmpty()) {
				String issueKey = issues.get(0);
				oldIssueKey = issueKey;
				Date started = oldWorklog.getWorkFrom();
				long duration = oldWorklog.getSeconds(); 
				String loginName = oldWorklog.getEmployee().getUser().getLogin().getLdapLogin();
				String comment = oldWorklog.getDescription();
				
				id = jiraConnector.findWorklogId(issueKey, started, duration, loginName, comment);
			}

			//no existing jira worklog => create new
			if (id == null) {
				addJiraWorklog(newWorklog);
				//that is all
				return;
			}
			
			issues = JiraHelper.findIssueCodesInText(newWorklog.getDescription());
			String newIssueKey = "new";
			if (issues != null && !issues.isEmpty()) {
				String issueKey = issues.get(0);
				newIssueKey = issueKey;
			}
				
			if (!oldIssueKey.equals(newIssueKey)) {
				//delete old
				if (!jiraConnector.deleteWorklog(oldIssueKey, id)) {
					logger.error("Couldn't delete log in JIRA.");
				}
				//create new
				addJiraWorklog(newWorklog);
				//that is all
				return;
			}
			
			// update
			if (issues != null && !issues.isEmpty()) {
				String issueKey = issues.get(0);
				Date started = newWorklog.getWorkFrom();
				long duration = newWorklog.getSeconds(); 
				String loginName = newWorklog.getEmployee().getUser().getLogin().getLdapLogin();
				String comment = newWorklog.getDescription();
						
				if (!jiraConnector.updateWorklog(issueKey, started, duration, loginName, comment, id)) {
					logger.error("Couldn't log in JIRA.");
				} else {
					getJiraIssueFromConnector(issueKey);
				}
			}
		}
	}

	@Override
	public void deleteJiraWorklog(Worklog oldWorklog) {
		if (isJiraEnabled()) {
			String id = null;
			List<String> issues = JiraHelper.findIssueCodesInText(oldWorklog.getDescription());
			String oldIssueKey = "old";
			if (issues != null && !issues.isEmpty()) {
				String issueKey = issues.get(0);
				oldIssueKey = issueKey;
				Date started = oldWorklog.getWorkFrom();
				long duration = oldWorklog.getSeconds(); 
				String loginName = oldWorklog.getEmployee().getUser().getLogin().getLdapLogin();
				String comment = oldWorklog.getDescription();
				
				id = jiraConnector.findWorklogId(issueKey, started, duration, loginName, comment);
			}

			if (id != null) {
				//delete old
				if (!jiraConnector.deleteWorklog(oldIssueKey, id)) {
					logger.error("Couldn't delete log in JIRA.");
				}
			}
		}
	}
	
	public boolean isJiraEnabled() {
		if (jiraConfig != null && jiraConfig.isEnabled()) {
			return true;
		}
		
		return false;
	}

	private JiraIssue getJiraIssueFromConnector(final String code) {
		JiraIssue result = null;
		result = jiraConnector.getJiraIssue(code);
		if (jiraConfig.getJiraCache() != null) {
			jiraConfig.getJiraCache().addIssueToCache(result);				
		}
		return result;
	}

}
