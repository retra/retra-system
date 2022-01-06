package cz.softinel.retra.worklog.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.Assert;

import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.InvoiceHelper;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogViewOverview;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

public class HibernateWorklogDao extends AbstractHibernateDao implements WorklogDao {

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#get(java.lang.Long)
	 */
	public Worklog get(Long pk) {
		// check if project id is defined
		Assert.notNull(pk);

		Worklog worklog = (Worklog) getHibernateTemplate().get(Worklog.class, pk);
		return worklog;
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#insert(cz.softinel.retra.worklog.Worklog)
	 */
	public Worklog insert(Worklog worklog) {
		getHibernateTemplate().save(worklog);
		return worklog;
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#update(cz.softinel.retra.worklog.Worklog)
	 */
	public void update(Worklog worklog) {
		getHibernateTemplate().update(worklog);
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#merge(cz.softinel.retra.worklog.Worklog)
	 */
	public void merge(Worklog worklog) {
		getHibernateTemplate().merge(worklog);
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#delete(cz.softinel.retra.worklog.Worklog)
	 */
	public void delete(Worklog worklog) {
		getHibernateTemplate().delete(worklog);
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#findAll()
	 */
	public List<Worklog> selectAll() {
		@SuppressWarnings("unchecked")
		List<Worklog> result = getHibernateTemplate().loadAll(Worklog.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#selectForEmployee(java.lang.Long)
	 */
	public List<Worklog> selectForEmployee(Long pk) {
		Assert.notNull(pk);
		@SuppressWarnings("unchecked")
		List<Worklog> result = getHibernateTemplate().findByNamedQueryAndNamedParam("Worklog.selectForEmployee", "pk",
				pk);
		return result;
	}

	public List<Worklog> selectForInvoice(Long pk) {
		Assert.notNull(pk);
		@SuppressWarnings("unchecked")
		List<Worklog> result = getHibernateTemplate().findByNamedQueryAndNamedParam("Worklog.selectForInvoice", "pk",
				pk);
		return result;
	}

	/**
	 * @see cz.softinel.retra.worklog.dao.WorklogDao#load(cz.softinel.retra.worklog.Worklog)
	 */
	public void load(Worklog worklog) {
		// check if worklog is defined and has defined pk and than load it
		Assert.notNull(worklog);
		Assert.notNull(worklog.getPk());
		getHibernateTemplate().load(worklog, worklog.getPk());
	}

	public void loadAndLoadLazy(Worklog worklog) {
		Session session = getSession();
		session.load(worklog, worklog.getPk());
		if (worklog.getIssueTrackingWorklogs() != null) {
			worklog.getIssueTrackingWorklogs().size();
		}
		releaseSession(session);
	}

	public List<Worklog> selectByFilter(Filter filter) {
		Assert.notNull(filter);
		return filterWorklogs(filter);
	}

	public List<Worklog> selectForEmployeeInPeriod(Filter filter) {
		return selectForEmployeeInPeriod(filter, false);
	}

	public List<Worklog> selectForEmployeeInPeriodExclude(Filter filter) {
		return selectForEmployeeInPeriod(filter, true);
	}

	public List<WorklogViewOverview> selectWorklogOverviewByFilter(Filter filter) {
		Assert.notNull(filter);
		return getQueryWorklogOverview(filter);
	}

	@SuppressWarnings("unchecked")
	private List<Worklog> selectForEmployeeInPeriod(Filter filter, boolean exclude) {
		Assert.notNull(filter);

		Long employeePk = FilterHelper.getFieldAsLong(WorklogFilter.WORKLOG_FILTER_EMPLOYEE, filter);
		Date from = FilterHelper.getFieldAsDate(WorklogFilter.WORKLOG_FILTER_FROM, filter);
		Date to = FilterHelper.getFieldAsDate(WorklogFilter.WORKLOG_FILTER_TO, filter);

		Assert.notNull(employeePk);
		Assert.notNull(from);
		Assert.notNull(to);

		Session session = getSession();
		Query query = null;
		if (exclude) {
			query = session.getNamedQuery("Worklog.selectForEmployeeInPeriodExclude");
		} else {
			query = session.getNamedQuery("Worklog.selectForEmployeeInPeriod");
		}
		query.setLong("employeePk", employeePk);
		query.setTimestamp("dateFrom", from);
		query.setTimestamp("dateTo", to);

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Worklog> filterWorklogs(Filter filter) {
		Long employeePk = FilterHelper.getFieldAsLong(WorklogFilter.WORKLOG_FILTER_EMPLOYEE, filter);
//		Long mainProjectPk = FilterHelper.getFieldAsLong(WorklogFilter.WORKLOG_FILTER_MAIN_PROJECT, filter);
		Long projectPk = FilterHelper.getFieldAsLong(WorklogFilter.WORKLOG_FILTER_PROJECT, filter);
		Long activityPk = FilterHelper.getFieldAsLong(WorklogFilter.WORKLOG_FILTER_ACTIVITY, filter);
		Date date = FilterHelper.getFieldAsDate(WorklogFilter.WORKLOG_FILTER_DATE, filter);
		Date from = FilterHelper.getFieldAsDate(WorklogFilter.WORKLOG_FILTER_FROM, filter);
		Date to = FilterHelper.getFieldAsDate(WorklogFilter.WORKLOG_FILTER_TO, filter, true);
		if (date != null && from == null && to == null) {
			from = date;
			// TODO: It is clear?
			to = FilterHelper.getFieldAsDate(WorklogFilter.WORKLOG_FILTER_DATE, filter, true);
		}
		Boolean onInvoice = FilterHelper.getFieldAsBoolean(WorklogFilter.WORKLOG_FILTER_ON_INVOICE, filter);
		String invoiceCode = getLikeValue(
				FilterHelper.getFieldAsString(WorklogFilter.WORKLOG_FILTER_INVOICE_CODE, filter));
		String invoiceRelation = getLikeValue(
				FilterHelper.getFieldAsString(WorklogFilter.WORKLOG_FILTER_INVOICE_RELATION, filter));

		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" worklog ");
		sb.append("from ");
		sb.append(" Worklog as worklog ");
		sb.append(" left join fetch worklog.employee ");
		sb.append(" left join fetch worklog.project ");
		sb.append(" left join fetch worklog.activity ");
		sb.append(" left join fetch worklog.invoice ");
		sb.append(" left join fetch worklog.employee.user ");
		sb.append(" left join fetch worklog.employee.user.login ");
		sb.append(" left join fetch worklog.employee.user.contactInfo ");
		sb.append(" left join fetch worklog.employee.user.personalRole ");

		sb.append("where 1=1 ");
		if (employeePk != null) {
			sb.append(" and worklog.employee.pk=:employeePk ");
		}
		if (projectPk != null) {
//			sb.append(" and worklog.project.pk=:projectPk ");
//			sb.append(" and worklog.project.parent.pk=:mainProjectPk ");
			// TODO radek: This is hack for emulate project hierarchical structure
			sb.append(" and ( (worklog.project.pk=:projectPk) or (worklog.project.parent.pk=:projectPk) ) ");
		}
		if (activityPk != null) {
			sb.append(" and worklog.activity.pk=:activityPk ");
		}
		if (from != null) {
			sb.append(" and worklog.workFrom >= :dateFrom");
		}
		if (to != null) {
			sb.append(" and worklog.workFrom <= :dateTo");
		}
		if (onInvoice != null) {
			if (onInvoice) {
				sb.append(" and worklog.invoice is not null null");
			} else {
				sb.append(" and worklog.invoice is null");
			}
		}
		if (invoiceCode != null) {
			sb.append(" and worklog.invoice.code like :invoiceCode ");
		}
		if (invoiceRelation != null) {
			if (InvoiceHelper.INVOICE_RELATION_CLOSED.equals(invoiceRelation)) {
				sb.append(" and worklog.invoice.state = " + Invoice.STATE_CLOSED + " ");
			} else if (InvoiceHelper.INVOICE_RELATION_NOT_CLOSED.equals(invoiceRelation)) {
				sb.append(" and (worklog.invoice is null or worklog.invoice.state = " + Invoice.STATE_ACTIVE + ") ");
			}
		}

		sb.append(" order by ");
		sb.append(" worklog.workFrom,");
		sb.append(" worklog.project,");
		sb.append(" worklog.activity ");

		Session session = getSession();
		Query query = session.createQuery(sb.toString());
		if (employeePk != null) {
			query.setLong("employeePk", employeePk);
		}
//		if (mainProjectPk != null) {
//			query.setLong("mainProjectPk", mainProjectPk);
//		}
		if (projectPk != null) {
			query.setLong("projectPk", projectPk);
		}
		if (activityPk != null) {
			query.setLong("activityPk", activityPk);
		}
		if (from != null) {
			query.setTimestamp("dateFrom", from);
		}
		if (to != null) {
			query.setTimestamp("dateTo", to);
		}
		if (invoiceCode != null) {
			query.setString("invoiceCode", invoiceCode);
		}

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	private List<WorklogViewOverview> getQueryWorklogOverview(Filter filter) {
		Long employeePk = FilterHelper.getFieldAsLong(WorklogFilter.WORKLOG_FILTER_EMPLOYEE, filter);
		Date from = FilterHelper.getFieldAsFirstDayDate(WorklogFilter.WORKLOG_FILTER_YEAR,
				WorklogFilter.WORKLOG_FILTER_MONTH, filter);
		Date to = FilterHelper.getFieldAsLastDayDate(WorklogFilter.WORKLOG_FILTER_YEAR,
				WorklogFilter.WORKLOG_FILTER_MONTH, filter);

		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" worklog ");
		sb.append("from ");
		sb.append(" WorklogViewOverview as worklog ");
		sb.append(" left join fetch worklog.employee ");
		sb.append(" left join fetch worklog.project ");
		sb.append(" left join fetch worklog.activity ");
		sb.append(" left join fetch worklog.employee.user ");
		sb.append(" left join fetch worklog.employee.user.login ");
		sb.append(" left join fetch worklog.employee.user.contactInfo ");
		sb.append(" left join fetch worklog.employee.user.personalRole ");
		sb.append("where 1=1 ");
		if (employeePk != null) {
			sb.append(" and worklog.employee.pk=:employeePk ");
		}
		if (from != null) {
			sb.append(" and	( ");
			sb.append("  worklog.date>=:dateFrom ");
			sb.append(" ) ");
		}
		if (to != null) {
			sb.append(" and	( ");
			sb.append("  worklog.date<=:dateTo ");
			sb.append(" ) ");
		}
		sb.append("order by ");
		sb.append(" worklog.date,");
		sb.append(" worklog.project,");
		sb.append(" worklog.activity ");

		Session session = getSession();
		Query query = session.createQuery(sb.toString());
		if (employeePk != null) {
			query.setLong("employeePk", employeePk);
		}
		if (from != null) {
			query.setTimestamp("dateFrom", from);
		}
		if (to != null) {
			query.setTimestamp("dateTo", to);
		}

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

}