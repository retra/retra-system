package cz.softinel.retra.schedule.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.Assert;

import cz.softinel.retra.schedule.Schedule;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

public class HibernateScheduleDao extends AbstractHibernateDao implements ScheduleDao {

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#get(java.lang.Long)
	 */
	public Schedule get(Long pk) {
		// check if schedule id is defined
		Assert.notNull(pk);

		Schedule schedule = (Schedule) getHibernateTemplate().get(Schedule.class, pk);
		return schedule;
	}

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#insert(cz.softinel.retra.schedule.Schedule)
	 */
	public Schedule insert(Schedule schedule) {
		getHibernateTemplate().save(schedule);
		return schedule;
	}

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#update(cz.softinel.retra.schedule.Schedule)
	 */
	public void update(Schedule schedule) {
		getHibernateTemplate().update(schedule);
	}
	
	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#merge(cz.softinel.retra.schedule.Schedule)
	 */
	public void merge(Schedule schedule) {
		getHibernateTemplate().merge(schedule);
	}

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#delete(cz.softinel.retra.schedule.Schedule)
	 */
	public void delete(Schedule schedule) {
		getHibernateTemplate().delete(schedule);
	}

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> selectAll() {
		return getHibernateTemplate().loadAll(Schedule.class);
	}

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#findAllForUser(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> selectAllForEmployee(Long pk) {
		Assert.notNull(pk);
		return getHibernateTemplate().findByNamedQueryAndNamedParam("Schedule.selectAllForEmployee", "pk", pk);
	}

	/**
	 * @see cz.softinel.retra.schedule.dao.ScheduleDao#load(cz.softinel.retra.schedule.Schedule)
	 */
	public void load(Schedule schedule) {
		// check if schedule is defined and has defined pk and than load it
		Assert.notNull(schedule);
		Assert.notNull(schedule.getPk());
		getHibernateTemplate().load(schedule, schedule.getPk());
	}

	@SuppressWarnings("unchecked")
	public List<Schedule> selectByFilter(Filter filter) {
		Assert.notNull(filter);
		return filterSchedules(filter);
	}

//	@SuppressWarnings("unchecked")
//	public List<ScheduleViewOverview> selectScheduleOverviewByFilter(Filter filter) {
//		Assert.notNull(filter);
//		return getQueryScheduleOverview(filter).list();
//	}

	@SuppressWarnings("unchecked")
	public List<Schedule> selectForEmployeeInPeriod(Filter filter) {
		Assert.notNull(filter);

		Long employeePk = FilterHelper.getFieldAsLong(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, filter);
		Date dateBegin = FilterHelper.getFieldAsDate(ScheduleFilter.SCHEDULE_FILTER_FROM, filter);
		Date dateEnd = FilterHelper.getFieldAsDate(ScheduleFilter.SCHEDULE_FILTER_TO, filter, true);

		Assert.notNull(employeePk);
		Assert.notNull(dateBegin);
		Assert.notNull(dateEnd);

		Session session = getSession();
		Query query = session.getNamedQuery("Schedule.selectForEmployeeInPeriod");
		query.setLong("employeePk", employeePk);
		query.setTimestamp("dateBegin", dateBegin);
		query.setTimestamp("dateEnd", dateEnd);
		
		try{
			return query.list();
		}finally{
			releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Schedule> filterSchedules(Filter filter){
		Long employeePk = FilterHelper.getFieldAsLong(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, filter);
		Long typePk = FilterHelper.getFieldAsLong(ScheduleFilter.SCHEDULE_FILTER_TYPE, filter);
		Date from = FilterHelper.getFieldAsDate(ScheduleFilter.SCHEDULE_FILTER_FROM, filter);
		if (from == null) {
			from = FilterHelper.getFieldAsFirstDayDate(ScheduleFilter.SCHEDULE_FILTER_YEAR, ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);
		}
		Date to = FilterHelper.getFieldAsDate(ScheduleFilter.SCHEDULE_FILTER_TO, filter, true);
		if (to == null) {
			to = FilterHelper.getFieldAsLastDayDate(ScheduleFilter.SCHEDULE_FILTER_YEAR, ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);
		}
		Integer state = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_STATE, filter);  
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" schedule ");
		sb.append("from ");
		sb.append(" Schedule as schedule ");
		sb.append(" left join fetch schedule.employee ");
		sb.append(" left join fetch schedule.type ");
		sb.append("where 1=1 ");
		if (employeePk != null) {
			sb.append(" and schedule.employee.pk=:employeePk ");
		}
		if (typePk != null) {
			sb.append(" and schedule.type.pk=:typePk ");
		}
		if (from != null && to != null) {
			sb.append(" and	( ");
			sb.append("  (schedule.workFrom>=:dateFrom and schedule.workFrom<=:dateTo) ");
			sb.append("  or (schedule.workTo>=:dateFrom and schedule.workTo<=:dateTo) ");
			sb.append("  or (schedule.workFrom<=:dateFrom and schedule.workTo>=:dateTo) ");
			sb.append(" ) ");
		}
		else {
			if (from != null) {
				sb.append(" and	( ");
				sb.append("  schedule.workFrom>=:dateFrom or schedule.workTo>=:dateFrom ");
				sb.append(" ) ");
			}
			if (to != null) {
				sb.append(" and	( ");
				sb.append("  schedule.workFrom<=:dateTo or schedule.workTo<=:dateTo ");
				sb.append(" ) ");
			}
		}
		if (state != null) {
			sb.append(" and state=:state ");
		}
		sb.append("order by ");
		sb.append(" schedule.workFrom,");
		sb.append(" schedule.type");
				
		Session session = getSession();
		Query query = session.createQuery(sb.toString());
		if (employeePk != null) {
			query.setLong("employeePk", employeePk);
		}
		if (typePk != null) {
			query.setLong("typePk", typePk);
		}
		if (from != null) {
			query.setTimestamp("dateFrom", from);
		}
		if (to != null) {
			query.setTimestamp("dateTo", to);
		}
		if (state != null) {
			query.setInteger("state", state);
		}
		try{
			return query.list();
		}finally{
			releaseSession(session);
		}
	}

//	private Query getQueryScheduleOverview(Filter filter){
//		Long employeePk = FilterHelper.getFieldAsLong(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, filter);
//		Date from = FilterHelper.getFieldAsFirstDayDate(ScheduleFilter.SCHEDULE_FILTER_YEAR, ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);
//		Date to = FilterHelper.getFieldAsLastDayDate(ScheduleFilter.SCHEDULE_FILTER_YEAR, ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append("select ");
//		sb.append(" schedule ");
//		sb.append("from ");
//		sb.append(" ScheduleViewOverview as schedule ");
//		sb.append(" left join fetch schedule.employee ");
//		sb.append(" left join fetch schedule.project ");
//		sb.append(" left join fetch schedule.activity ");
//		sb.append("where 1=1 ");
//		if (employeePk != null) {
//			sb.append(" and schedule.employee.pk=:employeePk ");
//		}
//		if (from != null) {
//			sb.append(" and	( ");
//			sb.append("  schedule.date>=:dateFrom ");
//			sb.append(" ) ");
//		}
//		if (to != null) {
//			sb.append(" and	( ");
//			sb.append("  schedule.date<=:dateTo ");
//			sb.append(" ) ");
//		}
//		sb.append("order by ");
//		sb.append(" schedule.date,");
//		sb.append(" schedule.project,");
//		sb.append(" schedule.activity ");
//
//		Session session = getSession();
//		Query query = session.createQuery(sb.toString());
//		if (employeePk != null) {
//			query.setLong("employeePk", employeePk);
//		}
//		if (from != null) {
//			query.setTimestamp("dateFrom", from);
//		}
//		if (to != null) {
//			query.setTimestamp("dateTo", to);
//		}
//		
//		return query;
//	}

}