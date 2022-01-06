package cz.softinel.retra.schedule.blo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.helper.DateHelper;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.schedule.ScheduleHelper;
import cz.softinel.retra.schedule.ScheduleWeekOverview;
import cz.softinel.retra.schedule.dao.ScheduleDao;
import cz.softinel.retra.schedule.dao.ScheduleFilter;
import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.messages.Message;

/**
 * This class is implementation of logic for schedule
 * 
 * @version $Revision: 1.5 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public class ScheduleLogicImpl extends AbstractLogicBean implements ScheduleLogic {

	private ScheduleDao scheduleDao;

	private MiraSecurityLogic securityLogic;

	/**
	 * @return the scheduleDao
	 */
	public ScheduleDao getScheduleDao() {
		return scheduleDao;
	}

	/**
	 * @param scheduleDao the scheduleDao to set
	 */
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}

	/**
	 * @return the securityLogic
	 */
	public MiraSecurityLogic getSecurityLogic() {
		return securityLogic;
	}

	/**
	 * @param securityLogic the securityLogic to set
	 */
	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		this.securityLogic = securityLogic;
	}

	/**
	 * @see cz.softinel.retra.schedule.blo.ScheduleLogic#findAllSchedules()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Schedule> findAllSchedules() {
		return scheduleDao.selectAll();
	}

	/**
	 * @see cz.softinel.retra.schedule.blo.ScheduleLogic#findAllSchedulesForEmployee(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Schedule> findAllSchedulesForEmployee(Long pk) {
		return scheduleDao.selectAllForEmployee(pk);
	}

	/**
	 * @see cz.softinel.retra.schedule.blo.ScheduleLogic#create(cz.softinel.retra.schedule.Schedule)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Schedule create(Schedule schedule) {
		return create(schedule, true);
	}

	/**
	 * @see cz.softinel.retra.schedule.blo.ScheduleLogic#remove(cz.softinel.retra.schedule.Schedule)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Schedule schedule) {
		// check if deleting just own schedule
		Long actualEmployeePk = getSecurityLogic().getLoggedEmployee().getPk();
		scheduleDao.load(schedule);
		if (!actualEmployeePk.equals(schedule.getEmployee().getPk())) {
			addError(new Message("schedule.error.delete.not.own"));
			return;
		}
		scheduleDao.delete(schedule);
	}

	/**
	 * @see cz.softinel.retra.schedule.blo.ScheduleLogic#remove(cz.softinel.retra.schedule.Schedule)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Long pk) {
		Schedule schedule = new Schedule();
		schedule.setPk(pk);
		remove(schedule);
	}

	/**
	 * @see cz.softinel.retra.schedule.blo.ScheduleLogic#get(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Schedule get(Long pk) {
		return scheduleDao.get(pk);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void store(Schedule schedule) {
		// bussines validation
		if (!hasValidTerm(schedule)) {
			Object[] parameters = new Object[] { DateConvertor.convertToDateStringFromDate(schedule.getWorkFrom()) };
			addError(new Message("schedule.has.invalid.terms", parameters));
			return;
		}
		// check if do not try to update not own schedule
		Long actualEmployeePk = getSecurityLogic().getLoggedEmployee().getPk();
		Schedule scheduleInDB = scheduleDao.get(schedule.getPk());
		if (!actualEmployeePk.equals(scheduleInDB.getEmployee().getPk())) {
			addError(new Message("schedule.error.update.not.own"));
			return;
		}
		// TODO: is transition allowed
		schedule.setState(ScheduleHelper.STATE_CHANGED);
		schedule.setChangedOn(new Date());
		scheduleDao.merge(schedule);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Schedule> findByFilter(Filter filter) {
		return scheduleDao.selectByFilter(filter);
	}

	// FIXME: make this method easier (separate into more smallets)
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ScheduleWeekOverview> findWeekOverviews(Filter filter) {
		Integer month = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);
		Integer year = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_YEAR, filter);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1, 0, 0, 0);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		int firstDay = 1;
		int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

		List<Schedule> list = scheduleDao.selectByFilter(filter);
		List<ScheduleWeekOverview> overViews = new ArrayList<ScheduleWeekOverview>();
		ScheduleWeekOverview scheduleWeekOverview = new ScheduleWeekOverview();
		overViews.add(scheduleWeekOverview);
		Iterator it = list.iterator();

		for (int i = 1; i <= (countEmptyDaysToGenerate(dayOfWeek)); i++) {
			Schedule newSchedule = new Schedule();
			switch (i) {
			case 1:
				scheduleWeekOverview.setMonday(newSchedule);
				break;
			case 2:
				scheduleWeekOverview.setTuesday(newSchedule);
				break;
			case 3:
				scheduleWeekOverview.setWednesday(newSchedule);
				break;
			case 4:
				scheduleWeekOverview.setThursday(newSchedule);
				break;
			case 5:
				scheduleWeekOverview.setFriday(newSchedule);
				break;
			case 6:
				scheduleWeekOverview.setSaturday(newSchedule);
				break;
			case 7:
				scheduleWeekOverview.setSunday(newSchedule);
				scheduleWeekOverview = new ScheduleWeekOverview();
				overViews.add(scheduleWeekOverview);
				break;
			}
		}

		for (int i = firstDay; i <= lastDay; i++) {
			Schedule schedule = null;
			if (it.hasNext()) {
				schedule = (Schedule) it.next();
				calendar.setTime(schedule.getWorkFrom());
				dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
				if (dayOfMonth != i) {
					int j = i;
					for (j = i; j <= dayOfMonth; j++) {
						Schedule newSchedule = new Schedule();
						Calendar helpCal = Calendar.getInstance();
						helpCal.set(year, month, j, 0, 0, 0);
						newSchedule.setWorkFrom(helpCal.getTime());
						int helpDayOfWeek = helpCal.get(Calendar.DAY_OF_WEEK);
						switch (helpDayOfWeek) {
						case Calendar.MONDAY:
							scheduleWeekOverview.setMonday(newSchedule);
							break;
						case Calendar.TUESDAY:
							scheduleWeekOverview.setTuesday(newSchedule);
							break;
						case Calendar.WEDNESDAY:
							scheduleWeekOverview.setWednesday(newSchedule);
							break;
						case Calendar.THURSDAY:
							scheduleWeekOverview.setThursday(newSchedule);
							break;
						case Calendar.FRIDAY:
							scheduleWeekOverview.setFriday(newSchedule);
							break;
						case Calendar.SATURDAY:
							scheduleWeekOverview.setSaturday(newSchedule);
							break;
						case Calendar.SUNDAY:
							scheduleWeekOverview.setSunday(newSchedule);
							scheduleWeekOverview = new ScheduleWeekOverview();
							overViews.add(scheduleWeekOverview);
							break;
						}
					}
					i = j - 1;
				}
				switch (dayOfWeek) {
				case Calendar.MONDAY:
					scheduleWeekOverview.setMonday(schedule);
					break;
				case Calendar.TUESDAY:
					scheduleWeekOverview.setTuesday(schedule);
					break;
				case Calendar.WEDNESDAY:
					scheduleWeekOverview.setWednesday(schedule);
					break;
				case Calendar.THURSDAY:
					scheduleWeekOverview.setThursday(schedule);
					break;
				case Calendar.FRIDAY:
					scheduleWeekOverview.setFriday(schedule);
					break;
				case Calendar.SATURDAY:
					scheduleWeekOverview.setSaturday(schedule);
					break;
				case Calendar.SUNDAY:
					scheduleWeekOverview.setSunday(schedule);
					scheduleWeekOverview = new ScheduleWeekOverview();
					overViews.add(scheduleWeekOverview);
					break;
				}
			} else {
				Schedule newSchedule = new Schedule();
				Calendar helpCal = Calendar.getInstance();
				helpCal.set(year, month, i, 0, 0, 0);
				newSchedule.setWorkFrom(helpCal.getTime());
				int helpDayOfWeek = helpCal.get(Calendar.DAY_OF_WEEK);
				switch (helpDayOfWeek) {
				case Calendar.MONDAY:
					scheduleWeekOverview.setMonday(newSchedule);
					break;
				case Calendar.TUESDAY:
					scheduleWeekOverview.setTuesday(newSchedule);
					break;
				case Calendar.WEDNESDAY:
					scheduleWeekOverview.setWednesday(newSchedule);
					break;
				case Calendar.THURSDAY:
					scheduleWeekOverview.setThursday(newSchedule);
					break;
				case Calendar.FRIDAY:
					scheduleWeekOverview.setFriday(newSchedule);
					break;
				case Calendar.SATURDAY:
					scheduleWeekOverview.setSaturday(newSchedule);
					break;
				case Calendar.SUNDAY:
					scheduleWeekOverview.setSunday(newSchedule);
					scheduleWeekOverview = new ScheduleWeekOverview();
					overViews.add(scheduleWeekOverview);
					break;
				}
				dayOfWeek = helpDayOfWeek;
			}
		}

		for (int i = 6; i > (countEmptyDaysToGenerate(dayOfWeek)); i--) {
			Schedule newSchedule = new Schedule();
			switch (i) {
			case 0:
				scheduleWeekOverview.setMonday(newSchedule);
				break;
			case 1:
				scheduleWeekOverview.setTuesday(newSchedule);
				break;
			case 2:
				scheduleWeekOverview.setWednesday(newSchedule);
				break;
			case 3:
				scheduleWeekOverview.setThursday(newSchedule);
				break;
			case 4:
				scheduleWeekOverview.setFriday(newSchedule);
				break;
			case 5:
				scheduleWeekOverview.setSaturday(newSchedule);
				break;
			case 6:
				scheduleWeekOverview.setSunday(newSchedule);
				break;
			}
		}

		return overViews;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void copyDay(Employee employee, Date source, Date destinationFrom, Date destinationTo) {
		Assert.notNull(employee);
		Assert.notNull(source);
		Assert.notNull(destinationFrom);
		Assert.notNull(destinationTo);

		// bussiness validation
		// this is check for existing terms of destination period, must be here, because
		// valid each term is too slow
		if (!couldCopy(employee, destinationFrom, destinationTo)) {
			addError(new Message("schedule.copy.impossible.existing"));
			return;
		}

		Date currentCopyDate = destinationFrom;

		Schedule copyFrom = findForEmployeeForDate(employee, source);
		if (copyFrom != null) {
			int createdCount = 0;
			int errorCount = 0;
			// while have copy to somewhere
			while (currentCopyDate.compareTo(destinationTo) <= 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(currentCopyDate);
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

				// do not copy to saturday and sunday
				if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
					createCopyOfSchedule(copyFrom, employee, currentCopyDate);
					if (errorCount == getErrors().size()) {
						createdCount++;
					} else {
						errorCount = getErrors().size();
					}
				}
				// increase date for 1 day
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				currentCopyDate = calendar.getTime();
			}
			addInfo(new Message("schedule.items.copied", new Object[] { createdCount }));
		} else {
			addWarning(new Message("schedule.copy.source.not.found"));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void copyWeek(Employee employee, Date source, Date destinationFrom, Date destinationTo) {
		Assert.notNull(employee);
		Assert.notNull(source);
		Assert.notNull(destinationFrom);
		Assert.notNull(destinationTo);

		// bussiness validation
		// this is check for existing terms of destination period, must be here, because
		// valid each term is too slow
		if (!couldCopy(employee, destinationFrom, destinationTo)) {
			addError(new Message("schedule.copy.impossible.existing"));
			return;
		}

		Date currentCopyDate = destinationFrom;

		Map<Integer, Schedule> copyFromWeek = new HashMap<Integer, Schedule>();

		// prepare copy froms
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);
		for (int i = 0; i < 7; i++) {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			Date date = calendar.getTime();
			Schedule copyFrom = findForEmployeeForDate(employee, date);
			copyFromWeek.put(dayOfWeek, copyFrom);

			if (copyFrom == null && (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY)) {
				addWarning(new Message("schedule.copy.source.not.found." + dayOfWeek));
			}
			if (copyFrom == null && (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
				addInfo(new Message("schedule.copy.source.not.found." + dayOfWeek));
			}

			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		// copy
		int createdCount = 0;
		int errorCount = 0;
		// while have copy to somewhere
		while (currentCopyDate.compareTo(destinationTo) <= 0) {
			calendar.setTime(currentCopyDate);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

			Schedule copyFrom = copyFromWeek.get(dayOfWeek);

			// do not copy if haven't source in map
			if (copyFrom != null) {
				createCopyOfSchedule(copyFrom, employee, currentCopyDate);
				if (errorCount == getErrors().size()) {
					createdCount++;
				} else {
					errorCount = getErrors().size();
				}
			}
			// increase date for 1 day
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			currentCopyDate = calendar.getTime();
		}
		addInfo(new Message("schedule.items.copied", new Object[] { createdCount }));
	}

	private Schedule findForEmployeeForDate(Employee employee, Date date) {
		Schedule result = null;
		Filter filter = new ScheduleFilter();
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, employee.getPk(), filter);
		date = DateHelper.getStartOfDay(date);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_FROM, date, filter);
		date = DateHelper.getEndOfDay(date);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_TO, date, filter);

		List<Schedule> list = scheduleDao.selectForEmployeeInPeriod(filter);
		// no match found -> null
		if (list.size() == 0) {
			result = null;
		}
		// something found get first item
		else {
			result = list.iterator().next();
			;
		}

		return result;
	}

	private boolean hasValidTerm(Schedule schedule) {
		boolean result = true;
		Filter filter = new ScheduleFilter();
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, schedule.getEmployee().getPk(), filter);
		Date date = schedule.getWorkFrom();
		date = DateHelper.getStartOfDay(date);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_FROM, date, filter);
		date = DateHelper.getEndOfDay(date);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_TO, date, filter);

		List<Schedule> list = scheduleDao.selectForEmployeeInPeriod(filter);
		// no match found - it is ok
		if (list.size() == 0) {
			result = true;
		} else if (list.size() == 1) {
			result = false;
			// take this only one item and compare ids
			Schedule existingSchedule = list.iterator().next();
			// it is the same object - just update it
			if (existingSchedule.getPk().equals(schedule.getPk())) {
				result = true;
			}
			// it is new object or updated to another object period
			else {
				result = false;
			}
		}
		// more than one existing conflict period
		else {
			result = false;
		}

		return result;
	}

	private boolean couldCopy(Employee employee, Date destinationFrom, Date destinationTo) {
		boolean result = true;
		Filter filter = new ScheduleFilter();
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, employee.getPk(), filter);
		Date date = destinationFrom;
		date = DateHelper.getStartOfDay(date);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_FROM, date, filter);
		// TODO: move to helper
		date = destinationTo;
		date = DateHelper.getEndOfDay(date);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_TO, date, filter);

		List<Schedule> list = scheduleDao.selectForEmployeeInPeriod(filter);
		// no match found - it is ok
		if (list.size() == 0) {
			result = true;
		}
		// more than one existing conflict period
		else {
			result = false;
		}

		return result;
	}

	private Date getCopyDate(Date scheduleDate, Date dateForPrepare) {
		Date result = null;

		// from date from prepare need time
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(scheduleDate);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millisecond = calendar.get(Calendar.MILLISECOND);

		// from scheduleDate need date and put there time
		calendar.setTime(dateForPrepare);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, millisecond);

		result = calendar.getTime();

		return result;
	}

	private void createCopyOfSchedule(Schedule sourceSchedule, Employee employee, Date copyDate) {
		Schedule schedule = new Schedule();
		schedule.setEmployee(employee);
		schedule.setLevel(sourceSchedule.getLevel());
		schedule.setType(sourceSchedule.getType());

		Date from = getCopyDate(sourceSchedule.getWorkFrom(), copyDate);
		Date to = getCopyDate(sourceSchedule.getWorkTo(), copyDate);

		schedule.setWorkFrom(from);
		schedule.setWorkTo(to);

		// prepared -> create - without check, it is too slow - check all above
		create(schedule, false);
	}

	private Schedule create(Schedule schedule, boolean checkTerms) {
		// bussines validation
		if (checkTerms && !hasValidTerm(schedule)) {
			Object[] parameters = new Object[] { DateConvertor.convertToDateStringFromDate(schedule.getWorkFrom()) };
			addError(new Message("schedule.has.invalid.terms", parameters));
			return null;
		}
		// TODO: hardcoded level (todo, maybe not), state (holidays not approve
		// automatic)
		schedule.setLevel(ScheduleHelper.LEVEL_DETAIL_PLAN);
		schedule.setState(ScheduleHelper.STATE_APPROVED);
		schedule.setCreatedOn(new Date());
		return scheduleDao.insert(schedule);
	}

	private int countEmptyDaysToGenerate(int dayOfWeek) {
		int result = 0;
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			result = 0;
			break;
		case Calendar.TUESDAY:
			result = 1;
			break;
		case Calendar.WEDNESDAY:
			result = 2;
			break;
		case Calendar.THURSDAY:
			result = 3;
			break;
		case Calendar.FRIDAY:
			result = 4;
			break;
		case Calendar.SATURDAY:
			result = 5;
			break;
		case Calendar.SUNDAY:
			result = 6;
			break;
		}
		return result;
	}
}
