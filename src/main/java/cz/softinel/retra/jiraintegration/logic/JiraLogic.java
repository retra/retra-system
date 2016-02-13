package cz.softinel.retra.jiraintegration.logic;

import java.util.List;

import cz.softinel.retra.jiraintegration.JiraConfig;
import cz.softinel.retra.jiraintegration.JiraIssue;
import cz.softinel.retra.worklog.Worklog;

public interface JiraLogic {

	public boolean isJiraEnabled();
	
	public JiraConfig getJiraConfig();
	
	public List<JiraIssue> findJiraIssuesForUser(String ldapLogin);
	
	public JiraIssue getJiraIssue(String code);
	
	public void addJiraWorklog(Worklog worklog);
	
	public void updateJiraWorklog(Worklog oldWorklog, Worklog newWorklog);
	
	public void deleteJiraWorklog(Worklog oldWorklog);
}
