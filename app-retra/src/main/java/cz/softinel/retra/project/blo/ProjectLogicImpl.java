package cz.softinel.retra.project.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.dao.ProjectDao;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.uaf.filter.Filter;


/**
 * Implementation of project logic
 * 
 * @version $Revision: 1.5 $ $Date: 2007-08-19 19:51:55 $
 * @author Radek Pinc, Petr Sígl
 */
public class ProjectLogicImpl extends AbstractLogicBean implements ProjectLogic {

	private ProjectDao projectDao;
	
	/**
	 * @return the projectDao
	 */
	public ProjectDao getProjectDao() {
		return projectDao;
	}

	/**
	 * @param projectDao the projectDao to set
	 */
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	/**
	 * @see cz.softinel.retra.project.blo.ProjectLogic#findAllProjects()
	 */
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Project> findAllProjects() {
		return projectDao.selectAll();
	}

	/**
	 * @see cz.softinel.retra.project.blo.ProjectLogic#findAllNotDeletedProjects()
	 */
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Project> findAllNotDeletedProjects() {
		return projectDao.selectAllNotDeleted();
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Project> findByFilter(Filter filter) {
		return projectDao.selectByFilter(filter);
	}
	
	public Project create(Project project) {
		// Business validation ...
		// TODO: Duplicit name ...
		// TODO: Duplicit code ...

		return projectDao.insert(project);
	}

	public List<Project> findAllParentProjects() {
		return projectDao.selectAllParentProjects();
	}

	public List<Project> findProjectsWithCode(String code) {
		return projectDao.selectProjectsWithCode(code);
	}

	public Project get(Long pk) {
		return projectDao.get(pk);
	}
	
	public void loadAndLoadLazy(Project project) {
		projectDao.loadAndLoadLazy(project);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void store(Project project) {
		projectDao.update(project);
	}
}
