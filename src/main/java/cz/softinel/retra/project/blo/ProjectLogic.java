package cz.softinel.retra.project.blo;

import java.util.List;

import cz.softinel.retra.project.Project;
import cz.softinel.uaf.filter.Filter;

/**
 * this class represents all logic for projects.
 *
 * @version $Revision: 1.4 $ $Date: 2007-08-19 19:51:55 $
 * @author Petr Sígl
 */
public interface ProjectLogic {

	/**
	 * Find all project items.
	 * 
	 * @return
	 */
	public List<Project> findAllProjects();

	/**
	 * Find all project items, which are not deleted.
	 * 
	 * @return
	 */
	public List<Project> findAllNotDeletedProjects();

	public List<Project> findAllActiveProjects();

	public List<Project> findAllProjectsInWhichCouldDoWorkLog();

	/**
	 * Find all parent projects. It means parent == null and state == ACTIVE;
	 * 
	 * @return
	 */
	public List<Project> findAllParentProjects();

	public List<Project> findProjectsWithCode(String code);

	/**
	 * Find project according to given filter parameters
	 * 
	 * @param filter
	 * @return
	 */
	public List<Project> findByFilter(Filter filter);

	public Project create(Project project);

	/**
	 * Get project according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public Project get(Long pk);

	/**
	 * Load informations about project (defined by pk)
	 * 
	 * @param project where load and where is pk set
	 */
	public void loadAndLoadLazy(Project project);

	/**
	 * Stores given Project
	 * 
	 * @param Project
	 */
	public void store(Project project);

}
