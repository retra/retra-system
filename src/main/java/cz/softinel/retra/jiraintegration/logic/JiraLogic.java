package cz.softinel.retra.jiraintegration.logic;

import java.util.List;

import cz.hb.lib.jira.model.JiraIssue;
import cz.softinel.retra.jiraintegration.JiraConfig;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.sis.user.User;

public interface JiraLogic {

	public boolean isJiraEnabled();

	public JiraConfig getJiraConfig();

	public List<JiraIssue> findJiraIssuesForUser(User user);

	public JiraIssue getJiraIssue(String code);

	public void addJiraWorklog(Worklog worklog);

	public void updateJiraWorklog(Worklog oldWorklog, Worklog newWorklog);

	public void deleteJiraWorklog(Worklog oldWorklog);
}
