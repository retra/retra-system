package cz.softinel.retra.jiraintegration.worklog.service;

import java.util.List;

import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;

/**
 * Interface for the {@link JiraWorklog} manager.
 * 
 * @author SzalaiErik
 */
@Deprecated
public interface JiraWorklogLogic {

	/**
	 * Create new jiraworklog
	 */
	public JiraWorklog create(JiraWorklog jiraWorklog);

	/**
	 * Update the JiraWorklog
	 * 
	 * @param jiraWorklog
	 */
	public void update(JiraWorklog jiraWorklog);

	/**
	 * This will delete the worklog from the database.
	 * 
	 * @param jiraWorklog
	 */
	void delete(JiraWorklog jiraWorklog);

	/**
	 * Find all items, which are still not added.
	 * 
	 * @return
	 */
	public List<JiraWorklog> findAllNotAdded();

	/**
	 * This method returns all the jiraworklog elements to an edited retra worklog
	 * whi
	 * 
	 * @return
	 */
	public List<JiraWorklog> findAllNotAddedUpdated();

	/**
	 * This will return all the {@link JiraWorklog}s which have their issue updated.
	 * 
	 * @return
	 */
	public List<JiraWorklog> findAllWithIssueUpdated();

	/**
	 * Will find all JiraWorklogs where the worklog reference is null (must be
	 * deleted).
	 * 
	 * @return
	 */
	public List<JiraWorklog> findAllToBeDeleted();

	/**
	 * @return All the existing {@link JiraWorklog}s.
	 */
	public List<JiraWorklog> findAllJiraWorklogs();

}
