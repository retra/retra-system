package cz.softinel.retra.jiraintegration.worklog.dao;

import java.util.List;

import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;

/**
 * Retra can add worklogs to JIRA and a table represents the relation between RETRA and JIRA.
 * <p>Known implementation: {@link HibernateJiraWorklogDao}
 * @author Zoltan Vadasz
 * @author Erik Szalai
 * @see JiraWorklog
 */
public interface JiraWorklogDao {

	/**
	 * Insert JiraWorklog.
	 * @param jiraWorklog to insert
	 */
	public JiraWorklog insert(JiraWorklog jiraWorklog);
	
	/**
	 * Select all Jira worklogs by state.
	 * @param state
	 * @return
	 * @author Erik Szalai
	 */
	public List<JiraWorklog> selectByState(int state);
	
	/**
	 * Select all jira worklogs by these states.
	 * @param states
	 * @return
	 */
	public List<JiraWorklog> selectByStates(Object[] states);
	
	/**
	 * Return a list of JiraWorklogs which are still not added to JIRA
	 * @return a list of JiraWorkLog
	 * @see JiraWorklog#CREATED
	 */
	public List<JiraWorklog> selectAllNotAdded();
	
	/**
	 * Will select all which where updated and not added into Jira yet.
	 * @see JiraWorklog#UPDATED
	 */
	public List<JiraWorklog> selectAllNotAddedUpdated();
	
	/**
	 * Select all where worklog is null. (They will be deleted.)
	 * @return
	 */
	public List<JiraWorklog> selectAllWhereWorklogIsNull();
	
	/**
	 * Selects all worklogs.
	 * @return
	 */
	public List<JiraWorklog> selectAll();
	
	/**
	 * Update proxying.
	 * @param jiraWorklog
	 */
	public void update(JiraWorklog jiraWorklog);
	
	/**
	 * Delete proxying.
	 * @param primaryKey
	 */
	public void delete(JiraWorklog jiraWorklog);

}
