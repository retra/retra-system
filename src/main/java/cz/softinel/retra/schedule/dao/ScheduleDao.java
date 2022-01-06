package cz.softinel.retra.schedule.dao;

import java.util.List;

import cz.softinel.retra.schedule.Schedule;
import cz.softinel.uaf.filter.Filter;

/**
 * Dao for schedule schedules
 *
 * @version $Revision: 1.2 $ $Date: 2007-02-16 23:16:32 $
 * @author Petr Sígl
 */
public interface ScheduleDao {

	/**
	 * Returns schedule according to primary key.
	 * 
	 * @param pk primary key of schedule
	 * @return
	 */
	public Schedule get(Long pk);

	/**
	 * Insert Schedule
	 * 
	 * @param schedule to insert
	 */
	public Schedule insert(Schedule schedule);

	/**
	 * Update schedule
	 * 
	 * @param schedule to update
	 */
	public void update(Schedule schedule);

	/**
	 * Merges schedule
	 * 
	 * @param schedule schedule to update
	 */
	public void merge(Schedule schedule);

	/**
	 * Delete schedule
	 * 
	 * @param schedule to delete
	 */
	public void delete(Schedule schedule);

	/**
	 * Returns all schedules
	 * 
	 * @return all schedules
	 */
	public List<Schedule> selectAll();

	/**
	 * Returns all schedules
	 * 
	 * @return all schedules
	 */
	public List<Schedule> selectAllForEmployee(Long pk);

	/**
	 * Load informations about schedule (defined by pk)
	 * 
	 * @param schedule where load and where is pk set
	 */
	public void load(Schedule schedule);

	public List<Schedule> selectForEmployeeInPeriod(Filter filter);

	public List<Schedule> selectByFilter(Filter filter);

}
