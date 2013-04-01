package cz.softinel.retra.project.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.Assert;

import cz.softinel.retra.invoice.dao.InvoiceFilter;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogicImpl;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

public class HibernateProjectDao extends AbstractHibernateDao implements ProjectDao {

	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#get(java.lang.Long)
	 */
	public Project get(Long pk) {
		// check if project id is defined
		Assert.notNull(pk);

		Project project = (Project) getHibernateTemplate().get(Project.class, pk);
		return project;
	}
	
	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#insert(cz.softinel.retra.project.Project)
	 */	
	public Project insert(Project project) {
		getHibernateTemplate().save(project);
		return project;
	}

	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#update(cz.softinel.retra.project.Project)
	 */
	public void update(Project project) {
		getHibernateTemplate().update(project);
	}

	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#delete(cz.softinel.retra.project.Project)
	 */
	public void delete(Project project) {
		//TODO: is there some better way, to do delete - without load?
		//must be at first loaded
		load(project);
		//than deleted
		getHibernateTemplate().delete(project);
	}

	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#findAll()
	 */	
	public List<Project> selectAll() {		
		@SuppressWarnings("unchecked")
		List<Project> result = getHibernateTemplate().loadAll(Project.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#selectAllNotDeleted()
	 */
	@SuppressWarnings("unchecked")
	public List<Project> selectAllNotDeleted() {
		Object[] states = new Object[] {Project.STATE_DELETED}; 

		Session session = getSession();
		Query query = session.getNamedQuery("Project.selectAllWithoutStates");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> selectByState(int state) {
		Session session = getSession();
		Query query = session.getNamedQuery("Project.selectAllByState");
		query.setParameter("state",state);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}	
	
	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#load(cz.softinel.retra.project.Project)
	 */
	public void load(Project project) {
		// check if activity is defined and has defined pk and than load it
		Assert.notNull(project);
		Assert.notNull(project.getPk());
		getHibernateTemplate().load(project, project.getPk());
	}

	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#load(cz.softinel.retra.project.Project)
	 */
	public void loadAndLoadLazy(Project project) {
		Session session = getSession();
		session.load(project, project.getPk());
		if (project.getEmployees() != null) {
			project.getEmployees().size();
		}
		if (project.getComponents() != null) {
			project.getComponents().size();
		}
		releaseSession(session);
	}
		
	/**
	 * @see cz.softinel.retra.project.dao.ProjectDao#selectAllParentProjects()
	 */
	@SuppressWarnings("unchecked")
	public List<Project> selectAllParentProjects() {
		Object[] states = new Object[] {Project.STATE_DELETED}; 

		Session session = getSession();
		Query query = session.getNamedQuery("Project.selectAllParentProjects");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Project> selectProjectsWithCode(String code) {	
		Session session = getSession();
		Query query = session.getNamedQuery("Project.selectProjectsWithCode");
		query.setParameter("code", code);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}

	public List<Project> selectByFilter(Filter filter) {
		Assert.notNull(filter);
		return filterProjects(filter);
	}
	
	@SuppressWarnings("unchecked")
	private List<Project> filterProjects(Filter filter) {
		Long employeePk = FilterHelper.getFieldAsLong(ProjectFilter.PROJECT_FILTER_EMPLOYEE, filter);
		String code = getLikeValue(FilterHelper.getFieldAsString(ProjectFilter.PROJECT_FILTER_CODE, filter));
		String name = getLikeValue(FilterHelper.getFieldAsString(ProjectFilter.PROJECT_FILTER_NAME, filter));
		Long parentPk = FilterHelper.getFieldAsLong(ProjectFilter.PROJECT_FILTER_PARENT, filter);
		Integer state = FilterHelper.getFieldAsInteger(ProjectFilter.PROJECT_FILTER_STATE, filter); 
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" project ");
		sb.append("from ");
		sb.append(" Project as project ");
		sb.append("where 1=1 ");
		if (employeePk != null && employeePk > 0) {
			sb.append(" and :employeePk member of project.employees");
		}
		if (state != null && state.intValue() >= 0) {
			sb.append(" and project.state = :state ");
		}
		if (code != null) {
			sb.append(" and project.code like :code ");
		}
		if (name != null) {
			sb.append(" and project.name like :name ");
		}
		if (parentPk != null) {
			sb.append(" and project.parent.pk = :parentPk");
		}		
		
		Session session = getSession();
		Query query = session.createQuery(sb.toString());
		if (employeePk != null && employeePk > 0) {
			query.setLong("employeePk", employeePk);
		}
		if (parentPk != null) {
			query.setLong("parentPk", parentPk);
		}		
		if (state != null && state.intValue() >= 0) {
			query.setInteger("state", state);
		}
		if (code != null) {
			query.setString("code", code);
		}
		if (name != null) {
			query.setString("name", name);
		}

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}
}