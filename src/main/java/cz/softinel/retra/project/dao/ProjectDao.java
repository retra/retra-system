package cz.softinel.retra.project.dao;

import java.util.List;

import cz.softinel.retra.project.Project;
import cz.softinel.uaf.filter.Filter;

/**
 * Dao for worklog projects
 *
 * @version $Revision: 1.3 $ $Date: 2007-08-19 19:51:55 $
 * @author Petr Sígl
 */
public interface ProjectDao {

	/**
	 * Returns project according to primary key.
	 * 
	 * @param pk primary key of project
	 * @return
	 */
	public Project get(Long pk);

	/**
	 * Insert Project
	 * 
	 * @param project to insert
	 */
	public Project insert(Project project);

	/**
	 * Update project
	 * 
	 * @param project to update
	 */
	public void update(Project project);

	/**
	 * Delete project
	 * 
	 * @param project to delete
	 */
	public void delete(Project project);

	/**
	 * Returns all projects
	 * 
	 * @return all projects
	 */
	public List<Project> selectAll();

	/**
	 * Returns all projects which are not deleted
	 * 
	 * @return all projects
	 */
	public List<Project> selectAllNotDeleted();

	/**
	 * Load informations about project (defined by pk)
	 * 
	 * @param project where load and where is pk set
	 */
	public void loadAndLoadLazy(Project project);

	/**
	 * Returns all parent projects
	 * 
	 * @return all projects
	 */
	public List<Project> selectAllParentProjects();

	/**
	 * @return projects with the given code
	 */
	public List<Project> selectProjectsWithCode(String code);

	/**
	 * Returns project by filter.
	 * 
	 * @param filter
	 * @return
	 */
	public List<Project> selectByFilter(Filter filter);

	public List<Project> selectByState(int state);
}
