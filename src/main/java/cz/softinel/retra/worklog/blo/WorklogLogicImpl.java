package cz.softinel.retra.worklog.blo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.dao.InvoiceDao;
import cz.softinel.retra.jiraintegration.JiraHelper;
import cz.softinel.retra.jiraintegration.logic.JiraLogic;
import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogViewOverview;
import cz.softinel.retra.worklog.dao.WorklogDao;
import cz.softinel.retra.worklog.dao.WorklogFilter;
import cz.softinel.sis.security.NoPermissionException;
import cz.softinel.sis.security.PermissionHelper;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.messages.Message;

/**
 * Implementation of worklog logic
 * 
 * @version $Revision: 1.15 $ $Date: 2007-12-03 18:42:29 $
 * @author Radek Pinc, Petr Sígl
 */
public class WorklogLogicImpl extends AbstractLogicBean implements WorklogLogic {

	private WorklogDao worklogDao;
	private InvoiceDao invoiceDao;
	private MiraSecurityLogic securityLogic;

	private JiraLogic jiraLogic;

	/**
	 * @param worklogDao the worklogDao to set
	 */
	public void setWorklogDao(WorklogDao worklogDao) {
		this.worklogDao = worklogDao;
	}

	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}

	/**
	 * @param securityLogic the securityLogic to set
	 */
	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		this.securityLogic = securityLogic;
	}

	public JiraLogic getJiraLogic() {
		return jiraLogic;
	}

	public void setJiraLogic(JiraLogic jiraLogic) {
		this.jiraLogic = jiraLogic;
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#findAllWorklogs()
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Worklog> findAllWorklogs() {
		List<Worklog> result = new ArrayList<Worklog>();
		if (hasAdminWorklogPermission()) {
			result = worklogDao.selectAll();
			updateDescriptionGui(result, true);
		}
		return result;
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#findAllWorklogsForEmployee(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Worklog> findAllWorklogsForEmployee(Long pk) {
		List<Worklog> result = new ArrayList<Worklog>();

		if (hasAdminWorklogPermission() || isGivenEmpLoggedEmployee(pk)) {

			result = worklogDao.selectForEmployee(pk);
			updateDescriptionGui(result, true);

		}
		return result;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Worklog> findAllWorklogsForInvoice(Long pk) {
		List<Worklog> result = new ArrayList<Worklog>();
		Invoice invoice = invoiceDao.get(pk);
		if (invoice != null) {
			if (hasAdminWorklogPermission() || isGivenEmpLoggedEmployee(invoice.getEmployee().getPk())) {
				result = worklogDao.selectForInvoice(pk);
				updateDescriptionGui(result, true);
			}
		}

		return result;
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#create(cz.softinel.retra.worklog.Worklog)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Worklog create(Worklog worklog) {
		// business validation
		if (!hasValidTerm(worklog)) {
			addError(new Message("worklog.has.invalid.terms"));
			return null;
		}

		Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();
		if (!hasValidInvoice(worklog, actualEmployeePk)) {
			addError(new Message("worklog.has.invalid.invoice"));
			return null;
		}

		return worklogCreate(worklog);
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#create(java.util.List,
	 *      com.mysql.jdbc.Messages)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int create(List<Worklog> worklogItems) {
		Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();

		int counter = 0;
		for (Worklog worklog : worklogItems) {
			if (!hasValidTerm(worklog)) {
				addError(new Message("worklog.has.invalid.terms"));
				continue;
			}
			if (!hasValidInvoice(worklog, actualEmployeePk)) {
				addError(new Message("worklog.has.invalid.invoice"));
				continue;
			}
			worklogCreate(worklog);
			counter++;
		}
		return counter;
	}

	/**
	 * Implementation of creating worklog.
	 * 
	 * @param worklog
	 * @return
	 */
	private Worklog worklogCreate(Worklog worklog) {
		Worklog result = worklogDao.insert(worklog);

		if (isJiraEnabled()) {
			jiraLogic.addJiraWorklog(worklog);
			updateDescriptionGui(result, true);
		}

		return result;
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#remove(cz.softinel.retra.worklog.Worklog)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Worklog worklog) {
		// check if deleting just own worklog
		Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();
		worklogDao.load(worklog);
		if (!hasOwnWorklogPermission(worklog)) {
			addError(new Message("worklog.error.delete.not.own"));
			return;
		}
		if (!hasValidInvoice(worklog, actualEmployeePk)) {
			addError(new Message("worklog.error.delete.invalid.invoice"));
			return;
		}

		if (isJiraEnabled()) {
			jiraLogic.deleteJiraWorklog(worklog);
		}
		worklogDao.delete(worklog);
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#remove(cz.softinel.retra.worklog.Worklog)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Long pk) {
		Worklog worklog = new Worklog();
		worklog.setPk(pk);
		remove(worklog);
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#get(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Worklog get(Long pk) {
		Worklog worklog = worklogDao.get(pk);
		if (hasAdminWorklogPermission() || hasOwnWorklogPermission(worklog)) {
			updateDescriptionGui(worklog, true);
			return worklog;
		}
		throw new NoPermissionException();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public void loadAndLoadLazy(Worklog worklog) {
		if (hasAdminWorklogPermission() || hasOwnWorklogPermission(worklog)) {
			if (worklog.getEmployee() == null) {
				loadAndLoadLazyImpl(worklog);
			}
			updateDescriptionGui(worklog, true);
			return;
		}
		throw new NoPermissionException();
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#store(cz.softinel.retra.worklog.Worklog,
	 *      cz.softinel.uaf.messages.Messages)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void store(Worklog worklog) {
		storeImpl(worklog, true);
	}

	@Override
	public void storeFromInvoice(Worklog worklog) {
		storeImpl(worklog, false);
	}

	private void storeImpl(Worklog worklog, boolean updateJira) {
		Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();

		// business validation
		if (!hasValidTerm(worklog)) {
			addError(new Message("worklog.has.invalid.terms"));
			return;
		}

		if (!hasValidInvoice(worklog, actualEmployeePk)) {
			addError(new Message("worklog.has.invalid.invoice"));
			return;
		}

//		this.jiraWorklogDao.update(worklog.getCurrentIssueTrackingWorklog());
		// check if do not try to update not own worklog
		Worklog worklogInDB = worklogDao.get(worklog.getPk());
		if (!hasOwnWorklogPermission(worklogInDB)) {
			addError(new Message("worklog.error.update.not.own"));
			return;
		}

		if (isJiraEnabled() && updateJira) {
			jiraLogic.updateJiraWorklog(worklogInDB, worklog);
			updateDescriptionGui(worklog, true);
		}

		worklogDao.merge(worklog);
		// worklogDao.update(worklog); //Does not work with two worklogs in the session.
	}

	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#findByFilter(cz.softinel.uaf.filter.Filter,
	 *      cz.softinel.uaf.messages.Messages)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Worklog> findByFilter(Filter filter) {
		List<Worklog> result = worklogDao.selectByFilter(filter);
		updateDescriptionGui(result, true);
		return result;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Worklog> findByFilter(Filter filter, boolean resolveJiraLink) {
		List<Worklog> result = worklogDao.selectByFilter(filter);
		updateDescriptionGui(result, resolveJiraLink);
		return result;
	}
	
	/**
	 * @see cz.softinel.retra.worklog.blo.WorklogLogic#findWorklogOverviewByFilter(cz.softinel.uaf.filter.Filter,
	 *      cz.softinel.uaf.messages.Messages)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<WorklogViewOverview> findWorklogOverviewByFilter(Filter filter) {
		return worklogDao.selectWorklogOverviewByFilter(filter);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void unpairWorklogWithInvoice(Worklog worklog, Invoice invoice) {
		Assert.notNull(worklog);
		Assert.notNull(invoice);
		Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();

		if (hasOwnWorklogPermission(worklog) && actualEmployeePk.equals(invoice.getEmployee().getPk())
				&& worklog.getInvoice().getPk().equals(invoice.getPk())) {
			worklog.setInvoice(null);
			storeFromInvoice(worklog);
		} else {
			addError(new Message("invoice.worklog.unpair.error"));
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void pairWorklogWithInvoice(Worklog worklog, Invoice invoice) {
		Assert.notNull(worklog);
		Assert.notNull(invoice);

		Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();
		boolean ok = pairWorklogWithInvoice(worklog, invoice, actualEmployeePk);
		if (!ok) {
			addError(new Message("invoice.worklog.pair.error"));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int pairWorklogWithInvoice(Long invoicePk, Long... worklogsPk) {
		int counter = 0;

		if (worklogsPk != null && worklogsPk.length > 0) {
			Long actualEmployeePk = securityLogic.getLoggedEmployee().getPk();
			Invoice invoice = invoiceDao.get(invoicePk);
			// for all try store
			for (Long wpk : worklogsPk) {
				Worklog worklog = worklogDao.get(wpk);
				boolean ok = pairWorklogWithInvoice(worklog, invoice, actualEmployeePk);
				if (ok) {
					counter++;
				}
			}
		}

		return counter;
	}

	private boolean pairWorklogWithInvoice(Worklog worklog, Invoice invoice, Long actualEmployeePk) {
		if (hasOwnWorklogPermission(worklog) && actualEmployeePk.equals(invoice.getEmployee().getPk())) {
			worklog.setInvoice(invoice);
			storeFromInvoice(worklog);
			return true;
		}

		return false;
	}

	private boolean hasValidTerm(Worklog worklog) {
		boolean result = true;
		Filter filter = new WorklogFilter();
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_EMPLOYEE, worklog.getEmployee().getPk(), filter);
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_FROM, worklog.getWorkFrom(), filter);
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_TO, worklog.getWorkTo(), filter);

		List<Worklog> list = worklogDao.selectForEmployeeInPeriodExclude(filter);
		// no match found - it is ok
		if (list.size() == 0) {
			result = true;
		} else if (list.size() == 1) {
			result = false;
			// take this only one item and compare ids
			Worklog existingWorklog = list.iterator().next();
			// it is the same object - just update it
			if (existingWorklog.getPk().equals(worklog.getPk())) {
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

	private boolean hasValidInvoice(Worklog worklog, Long loggedEmployeePk) {
		// existing
		if (worklog.getPk() != null && worklog.getPk().longValue() > 0) {
			Worklog originalWorklog = worklogDao.get(worklog.getPk());
			if (originalWorklog.getInvoice() == null || originalWorklog.getInvoice().getPk() == null) {
				return true;
			} else {
				if (!originalWorklog.getInvoice().getEmployee().getPk().equals(loggedEmployeePk)) {
					return false;
				}

				return originalWorklog.getInvoice().getState() == Invoice.STATE_ACTIVE;
			}
			// new
		} else {
			if (worklog.getInvoice() == null || worklog.getInvoice().getPk() == null) {
				return true;
			} else {
				worklog.setInvoice(invoiceDao.get(worklog.getInvoice().getPk()));
				if (!worklog.getInvoice().getEmployee().getPk().equals(loggedEmployeePk)) {
					return false;
				}

				return worklog.getInvoice().getState() == Invoice.STATE_ACTIVE;
			}
		}
	}

	private boolean isJiraEnabled() {
		return jiraLogic.isJiraEnabled();
	}

	private void updateDescriptionGui(List<Worklog> worklogs, boolean resolveJiraLink) {
		if (isJiraEnabled() && worklogs != null && !worklogs.isEmpty()) {
			for (Worklog worklog : worklogs) {
				updateDescriptionGuiImpl(worklog, resolveJiraLink);
			}
		}
	}

	private void updateDescriptionGui(Worklog worklog, boolean resolveJiraLink) {
		if (isJiraEnabled() && worklog != null) {
			updateDescriptionGuiImpl(worklog, resolveJiraLink);
		}
	}

	private void updateDescriptionGuiImpl(Worklog worklog, boolean resolveJiraLink) {
		String escapedDescription = StringEscapeUtils.escapeHtml(worklog.getDescription());
		if (resolveJiraLink) {
			worklog.setDescriptionGui(JiraHelper.getLinkableText(escapedDescription, jiraLogic));
		} else {
			worklog.setDescriptionGui(escapedDescription);
		}
	}

	private boolean isGivenEmpLoggedEmployee(Long givenPk) {
		Long loggedEmployeePk = securityLogic.getLoggedEmployee().getPk();

		// not logged employee
		if (!loggedEmployeePk.equals(givenPk)) {
			return false;
		}

		return true;
	}

	private boolean hasOwnWorklogPermission(final Worklog worklog) {
		if (worklog == null) {
			return false;
		}

		if (worklog.getEmployee() == null) {
			loadAndLoadLazyImpl(worklog);
		}

		Long worklogEmployeePk = worklog.getEmployee().getPk();

		// not owned worklog
		if (!isGivenEmpLoggedEmployee(worklogEmployeePk)) {
			return false;
		}

		return true;
	}

	private boolean hasAdminWorklogPermission() {
		// check permission
		boolean hasWorklogAdminPermission = securityLogic.hasPermission(PermissionHelper.PERMISSION_VIEW_ALL_WORKLOGS);
		// do not have admin worklog permission
		if (!hasWorklogAdminPermission) {
			return false;
		}

		return true;
	}

	private void loadAndLoadLazyImpl(Worklog worklog) {
		worklogDao.loadAndLoadLazy(worklog);
	}

}
