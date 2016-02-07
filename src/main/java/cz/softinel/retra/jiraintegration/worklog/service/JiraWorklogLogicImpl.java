package cz.softinel.retra.jiraintegration.worklog.service;

import java.util.List;

import cz.softinel.retra.jiraintegration.soap.JiraSoapClient;
import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;
import cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao;

/**
 * Manager class for working with JiraWorklogs.
 * The Jira SOAP import methods where moved to {@link JiraSoapClient}.
 * @author Zoltan Vadasz
 * @author Erik Szalai
 * @see JiraWorklog
 * @see JiraWorklogDaof
 */
@Deprecated
public class JiraWorklogLogicImpl implements JiraWorklogLogic {

	private JiraWorklogDao jiraWorklogDao;

	/**
	 * @return the jiraWorklogDao
	 */
	public JiraWorklogDao getJiraWorklogDao() {
		return jiraWorklogDao;
	}

	/**
	 * @param jiraWorklogDao the jiraWorklogDao to set
	 */
	public void setJiraWorklogDao(JiraWorklogDao jiraWorklogDao) {
		this.jiraWorklogDao = jiraWorklogDao;
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#create(cz.softinel.retra.jiraintegration.worklog.JiraWorklog)
	 */
	public JiraWorklog create(JiraWorklog jiraWorklog) {
		return jiraWorklogDao.insert(jiraWorklog);
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#update(cz.softinel.retra.jiraintegration.worklog.JiraWorklog)
	 */
	public void update(JiraWorklog jiraWorklog) {
		jiraWorklogDao.update(jiraWorklog);
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#findAllNotAdded()
	 */
	public List<JiraWorklog> findAllNotAdded() {
		return jiraWorklogDao.selectAllNotAdded();
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#findAllNotAddedUpdated()
	 */
	public List<JiraWorklog> findAllNotAddedUpdated() {
		return jiraWorklogDao.selectAllNotAddedUpdated();
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#findAllWithIssueUpdated()
	 */
	public List<JiraWorklog> findAllWithIssueUpdated() {
//		Object [] states = new Object[] {JiraWorklog.ISSUE_UPDATED, JiraWorklog.EXPORTED_FOR_NONEXISTENT };
		Object [] states = new Object[] {JiraWorklog.ISSUE_UPDATED};
		return jiraWorklogDao.selectByStates(states);
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#findAllJiraWorklogs()
	 */
	public List<JiraWorklog> findAllJiraWorklogs() {
		return jiraWorklogDao.selectAll();
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic#delete(cz.softinel.retra.jiraintegration.worklog.JiraWorklog)
	 */
	public void delete(JiraWorklog jiraWorklog) {
		jiraWorklogDao.delete(jiraWorklog);
	}

	public List<JiraWorklog> findAllToBeDeleted() {
		return jiraWorklogDao.selectAllWhereWorklogIsNull();
	}

}
