package cz.softinel.retra.schedule.blo;

import java.util.Date;
import java.util.List;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.schedule.ScheduleWeekOverview;
import cz.softinel.uaf.filter.Filter;

/**
 * This class contains all logic for schedule.
 * 
 * @version $Revision: 1.4 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public interface ScheduleLogic {

	/**
	 * Find all schedule items.
	 * 
	 * @return
	 */
	public List<Schedule> findAllSchedules();

	/**
	 * Find all schedule items for employee identified by pk.
	 * 
	 * @param pk of user
	 * @return
	 */
	public List<Schedule> findAllSchedulesForEmployee(Long pk);

	public List<Schedule> findByFilter(Filter filter);

	public List<ScheduleWeekOverview> findWeekOverviews(Filter filter);
	
	/**
	 * Creates schedule
	 * 
	 * @param schedule
	 * @return
	 */
	public Schedule create(Schedule schedule);

	/**
	 * Remove given schedule
	 * 
	 * @param schedule
	 */
	public void remove(Schedule schedule);

	/**
	 * Remove schedule with given pk
	 * 
	 * @param pk
	 */
	public void remove(Long pk);

	/**
	 * Get schedule according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public Schedule get(Long pk);

	/**
	 * Stores given schedule
	 * 
	 * @param schedule
	 */
	public void store(Schedule schedule);

	/**
	 * Create copy of schedule according to given date to destination, day by day
	 * 
	 * @param schedule
	 */
	public void copyDay(Employee employee, Date source, Date destinationFrom, Date destinationTo);
	
	/**
	 * Create copy of schedule according to given date (take whole week) to destination
	 * 
	 * @param schedule
	 */
	public void copyWeek(Employee employee, Date source, Date destinationFrom, Date destinationTo);
}
