package cz.softinel.retra.worklog.dao;

import java.util.List;

import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogViewOverview;
import cz.softinel.uaf.filter.Filter;

/**
 * Dao for worklog worklogs
 *
 * @version $Revision: 1.7 $ $Date: 2007-02-16 23:16:32 $
 * @author Petr Sígl
 */
public interface WorklogDao {

	/**
	 * Returns worklog according to primary key.
	 * 
	 * @param pk primary key of worklog
	 * @return
	 */
	public Worklog get(Long pk);

	/**
	 * Insert Worklog
	 * 
	 * @param worklog to insert
	 */
	public Worklog insert(Worklog worklog);

	/**
	 * Update worklog
	 * 
	 * @param worklog to update
	 */
	public void update(Worklog worklog);

	/**
	 * Updates the existing worklog
	 * 
	 * @param worklog to update
	 */
	public void merge(Worklog worklog);

	/**
	 * Delete worklog
	 * 
	 * @param worklog to delete
	 */
	public void delete(Worklog worklog);

	/**
	 * Returns all worklogs
	 * 
	 * @return all worklogs
	 */
	public List<Worklog> selectAll();

	/**
	 * Returns all worklogs
	 * 
	 * @return all worklogs
	 */
	public List<Worklog> selectForEmployee(Long pk);

	/**
	 * Returns all worklogs
	 * 
	 * @return all worklogs
	 */
	public List<Worklog> selectForInvoice(Long pk);

	/**
	 * Load informations about worklog (defined by pk)
	 * 
	 * @param worklog where load and where is pk set
	 */
	public void load(Worklog worklog);

	/**
	 * Get worklog according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public void loadAndLoadLazy(Worklog worklog);

	public List<Worklog> selectForEmployeeInPeriod(Filter filter);

	public List<Worklog> selectForEmployeeInPeriodExclude(Filter filter);

	public List<Worklog> selectByFilter(Filter filter);

	public List<WorklogViewOverview> selectWorklogOverviewByFilter(Filter filter);
}
