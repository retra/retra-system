package cz.softinel.retra.invoice.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.Assert;

import cz.softinel.retra.invoice.Invoice;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

public class HibernateInvoiceDao extends AbstractHibernateDao implements InvoiceDao {

	/**
	 * @see cz.softinel.retra.invoice.dao.InvoiceDao#get(java.lang.Long)
	 */
	public Invoice get(Long pk) {
		// check if invoice id is defined
		Assert.notNull(pk);

		Invoice invoice = (Invoice) getHibernateTemplate().get(Invoice.class, pk);
		return invoice;
	}
	
	/**
	 * @see cz.softinel.retra.invoice.dao.InvoiceDao#insert(cz.softinel.retra.invoice.Invoice)
	 */	
	public Invoice insert(Invoice invoice) {
		getHibernateTemplate().save(invoice);
		return invoice;
	}

	/**
	 * @see cz.softinel.retra.invoice.dao.InvoiceDao#update(cz.softinel.retra.invoice.Invoice)
	 */
	public void update(Invoice invoice) {
		getHibernateTemplate().update(invoice);
	}

	/**
	 * @see cz.softinel.retra.invoice.dao.InvoiceDao#findAll()
	 */	
	public List<Invoice> selectAll() {		
		@SuppressWarnings("unchecked")
		List<Invoice> result = getHibernateTemplate().loadAll(Invoice.class);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> selectAllForEmployee(Long employeePk) {
		Session session = getSession();
		Query query = session.getNamedQuery("Invoice.selectAllForEmployee");
		query.setParameter("employeePk", employeePk);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> selectAllForEmployeeActive(Long employeePk) {
		Object[] states = new Object[] {Invoice.STATE_ACTIVE}; 

		Session session = getSession();
		Query query = session.getNamedQuery("Invoice.selectAllForEmployeeInStates");
		query.setParameterList("states", states);
		query.setParameter("employeePk", employeePk);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Invoice> selectAllForEmployeeNotDeleted(Long employeePk) {
		Object[] states = new Object[] {Invoice.STATE_DELETED}; 

		Session session = getSession();
		Query query = session.getNamedQuery("Invoice.selectAllForEmployeeWithoutStates");
		query.setParameterList("states", states);
		query.setParameter("employeePk", employeePk);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Invoice> selectInvoicesForEmployeeWithCode(Long employeePk,	String code) {
		Session session = getSession();
		Query query = session.getNamedQuery("Invoice.selectInvoiceForEmployeeWithCode");
		query.setParameter("employeePk", employeePk);
		query.setParameter("code", code);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	public List<Invoice> selectByFilter(Filter filter) {
		Assert.notNull(filter);
		return filterInvoices(filter);
	}
	
	/**
	 * @see cz.softinel.retra.invoice.dao.InvoiceDao#load(cz.softinel.retra.invoice.Invoice)
	 */
	public void load(Invoice invoice) {
		// check if activity is defined and has defined pk and than load it
		Assert.notNull(invoice);
		Assert.notNull(invoice.getPk());
		getHibernateTemplate().load(invoice, invoice.getPk());
	}
	
	@SuppressWarnings("unchecked")
	private List<Invoice> filterInvoices(Filter filter) {
		Long employeePk = FilterHelper.getFieldAsLong(InvoiceFilter.INVOICE_FILTER_EMPLOYEE, filter);
		Date orderDateFrom = FilterHelper.getFieldAsDate(InvoiceFilter.INVOICE_FILTER_ORDER_DATE_FROM, filter);
		Date orderDateTo = FilterHelper.getFieldAsDate(InvoiceFilter.INVOICE_FILTER_ORDER_DATE_TO, filter);
		Date finishDateFrom = FilterHelper.getFieldAsDate(InvoiceFilter.INVOICE_FILTER_FINISH_DATE_FROM, filter);
		Date finishDateTo = FilterHelper.getFieldAsDate(InvoiceFilter.INVOICE_FILTER_FINISH_DATE_TO, filter);
		Integer state = FilterHelper.getFieldAsInteger(InvoiceFilter.INVOICE_FILTER_STATE, filter); 
		String code = getLikeValue(FilterHelper.getFieldAsString(InvoiceFilter.INVOICE_FILTER_CODE, filter));
		
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(" invoice ");
		sb.append("from ");
		sb.append(" Invoice as invoice ");
		sb.append(" left join fetch invoice.employee ");
		sb.append(" left join fetch invoice.employee.user ");
		sb.append(" left join fetch invoice.employee.user.login ");
		sb.append(" left join fetch invoice.employee.user.contactInfo ");
		sb.append(" left join fetch invoice.employee.user.personalRole ");

		sb.append("where 1=1 ");
		if (employeePk != null && employeePk > 0) {
			sb.append(" and invoice.employee.pk=:employeePk ");
		}
		if (orderDateFrom != null) {
			sb.append(" and invoice.orderDate >= :orderDateFrom ");
		}
		if (orderDateTo != null) {
			sb.append(" and invoice.orderDate <= :orderDateTo ");
		}
		if (finishDateFrom != null) {
			sb.append(" and invoice.finishDate >= :finishDateFrom ");
		}
		if (finishDateTo != null) {
			sb.append(" and invoice.finishDate <= :finishDateTo ");
		}
		if (state != null && state >= 0) {
			sb.append(" and invoice.state = :state ");
		}
		if (code != null) {
			sb.append(" and invoice.code like :code ");
		}
		
		sb.append(" order by ");
		sb.append(" invoice.code desc,");
		sb.append(" invoice.orderDate,");
		sb.append(" invoice.finishDate,");
		sb.append(" invoice.name ");

		Session session = getSession();
		Query query = session.createQuery(sb.toString());
		if (employeePk != null && employeePk > 0) {
			query.setLong("employeePk", employeePk);
		}
		if (orderDateFrom != null) {
			query.setTimestamp("orderDateFrom", orderDateFrom);
		}
		if (orderDateTo != null) {
			query.setTimestamp("orderDateTo", orderDateTo);
		}
		if (finishDateFrom != null) {
			query.setTimestamp("finishDateFrom", finishDateFrom);
		}
		if (finishDateTo != null) {
			query.setTimestamp("finishDateTo", finishDateTo);
		}
		if (state != null && state >= 0) {
			query.setInteger("state", state);
		}
		if (code != null) {
			query.setString("code", code);
		}

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

	
}