package cz.softinel.retra.jiraintegration.worklog.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;

/**
 * Hibernate implementation for the {@link JiraWorklogDao}
 * @author SzalaiErik
 */
public class HibernateJiraWorklogDao extends HibernateDaoSupport implements JiraWorklogDao {

	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#insert(cz.softinel.retra.jiraintegration.worklog.JiraWorklog)
	 */
	public JiraWorklog insert(JiraWorklog jiraWorklog) {
		getHibernateTemplate().save(jiraWorklog);
		return jiraWorklog;
	}

	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#selectAllNotAdded()
	 */
	@SuppressWarnings("unchecked")
	public List<JiraWorklog> selectAllNotAdded() {
		Object[] states = new Object[] {JiraWorklog.CREATED};  

		Session session = getSession();
		Query query = session.getNamedQuery("JiraWorklog.selectAllNotAdded");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}

	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#selectAllNotAddedUpdated()
	 */
	@SuppressWarnings("unchecked")
	public List<JiraWorklog> selectAllNotAddedUpdated() {
		Object[] states = new Object[] {JiraWorklog.UPDATED};  

		Session session = getSession();
		Query query = session.getNamedQuery("JiraWorklog.selectAllNotAdded");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#selectByState(int)
	 */
	@SuppressWarnings("unchecked")
	public List<JiraWorklog> selectByState(int state) {
		Object[] states = new Object[] {state};  

		Session session = getSession();
		Query query = session.getNamedQuery("JiraWorklog.selectAllNotAdded");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#selectByStates(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public List<JiraWorklog> selectByStates(Object[] states) {
		Session session = getSession();
		Query query = session.getNamedQuery("JiraWorklog.selectAllNotAdded");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#selectAll()
	 */
	@SuppressWarnings("unchecked")
	public List<JiraWorklog> selectAll() {		
		List<JiraWorklog> result = getHibernateTemplate().loadAll(JiraWorklog.class);
		return result;
	}

	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#update(cz.softinel.retra.jiraintegration.worklog.JiraWorklog)
	 */
	public void update(JiraWorklog jiraWorklog) {
		getHibernateTemplate().update(jiraWorklog);
	}

	/* (non-Javadoc)
	 * @see cz.softinel.retra.jiraintegration.worklog.dao.JiraWorklogDao#delete(cz.softinel.retra.jiraintegration.worklog.JiraWorklog)
	 */
	public void delete(JiraWorklog jiraWorklog) {
		this.getHibernateTemplate().delete(jiraWorklog);
	}

	@SuppressWarnings("unchecked")
	public List<JiraWorklog> selectAllWhereWorklogIsNull() {
		Session session = getSession();
		Query query = session.getNamedQuery("JiraWorklog.selectAllWhereWorklogIsNull");
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}

}
