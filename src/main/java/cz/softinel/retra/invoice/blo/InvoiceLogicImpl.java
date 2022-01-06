package cz.softinel.retra.invoice.blo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.dao.InvoiceDao;
import cz.softinel.retra.invoiceseq.blo.InvoiceSeqLogic;
import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.dao.WorklogDao;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.messages.Message;

/**
 * Implementation of invoice logic
 * 
 * @author Petr Sígl
 */
public class InvoiceLogicImpl extends AbstractLogicBean implements InvoiceLogic {

	private InvoiceDao invoiceDao;
	private WorklogDao worklogDao;
	private MiraSecurityLogic securityLogic;
	private InvoiceSeqLogic invoiceSeqLogic;
	private EmployeeLogic employeeLogic;

	private boolean codeGenerated = false;

	/**
	 * @return the invoiceDao
	 */
	public InvoiceDao getInvoiceDao() {
		return invoiceDao;
	}

	/**
	 * @param invoiceDao the invoiceDao to set
	 */
	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}

	public void setInvoiceSeqLogic(InvoiceSeqLogic invoiceSeqLogic) {
		this.invoiceSeqLogic = invoiceSeqLogic;
	}

	public void setWorklogDao(WorklogDao worklogDao) {
		this.worklogDao = worklogDao;
	}

	/**
	 * @param securityLogic the securityLogic to set
	 */
	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		this.securityLogic = securityLogic;
	}

	public boolean isCodeGenerated() {
		return codeGenerated;
	}

	public void setCodeGenerated(boolean codeGenerated) {
		this.codeGenerated = codeGenerated;
	}

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	/**
	 * @see cz.softinel.retra.invoice.blo.InvoiceLogic#findAllInvoices()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Invoice> findAllInvoices() {
		return invoiceDao.selectAll();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Invoice> findAllInvoicesForEmployee(Long employeePk) {
		return invoiceDao.selectAllForEmployee(employeePk);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Invoice> findAllNotDeletedInvoicesForEmployee(Long employeePk) {
		return invoiceDao.selectAllForEmployeeNotDeleted(employeePk);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Invoice> findAllActiveInvoicesForEmployee(Long employeePk) {
		return invoiceDao.selectAllForEmployeeActive(employeePk);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Invoice> findInvoicesForEmployeeWithCode(Long employeePk, String code) {
		return invoiceDao.selectInvoicesForEmployeeWithCode(employeePk, code);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Invoice> findByFilter(Filter filter) {
		return invoiceDao.selectByFilter(filter);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Invoice create(Invoice invoice) {
		return create(invoice, null);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Invoice create(Invoice invoice, Long sequencePk) {
		if (isCodeGenerated()) {
			if (sequencePk == null) {
				addError(new Message("invoice.error.create.no.sequence"));
				return null;
			} else {
				String genCode = invoiceSeqLogic.getNextCodeForSequence(sequencePk);
				if (genCode == null) {
					return null;
				} else {
					invoice.setCode(genCode);
				}
			}
		}

		return invoiceDao.insert(invoice);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public List<Invoice> batchCreate(Long sequencePk, String name, Date orderDate, Date finishDate,
			Long[] employeePks) {
		List<Invoice> invoices = new ArrayList<Invoice>();
		if (employeePks == null || employeePks.length <= 0 || !isCodeGenerated()) {
			return invoices;
		}

		if (sequencePk == null) {
			addError(new Message("invoice.error.create.no.sequence"));
			return null;
		} else {
			for (Long employeePk : employeePks) {
				Employee employee = employeeLogic.get(employeePk);

				if (employee != null) {
					Invoice invoice = new Invoice();
					invoice.setOrderDate(orderDate);
					invoice.setFinishDate(finishDate);
					invoice.setName(name);
					String genCode = invoiceSeqLogic.getNextCodeForSequenceIgnoreStep(sequencePk);
					invoice.setCode(genCode);

					invoice.setEmployee(employee);

					Invoice ires = invoiceDao.insert(invoice);
					invoices.add(ires);
				}
			}
		}

		return invoices;
	}

	public Invoice get(Long pk) {
		Invoice invoice = new Invoice();
		invoice.setPk(pk);
		invoiceDao.loadAndLoadLazy(invoice);
		return invoice;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void store(Invoice invoice) {
		if (checkOwnership(invoice)) {
			invoiceDao.update(invoice);
		} else {
			addError(new Message("invoice.error.update.not.own"));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Invoice invoice) {
		List<Worklog> worklogs = worklogDao.selectForInvoice(invoice.getPk());

		if (worklogs != null && !worklogs.isEmpty()) {
			addError(new Message("invoice.error.delete.not.empty"));
			return;
		}

		if (!updateState(invoice, Invoice.STATE_DELETED)) {
			addError(new Message("invoice.error.delete.not.own"));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void close(Invoice invoice) {
		if (!updateState(invoice, Invoice.STATE_CLOSED)) {
			addError(new Message("invoice.error.close.not.own"));
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void open(Invoice invoice) {
		if (!updateState(invoice, Invoice.STATE_ACTIVE)) {
			addError(new Message("invoice.error.open.not.own"));
		}
	}

	private boolean updateState(Invoice invoice, int state) {
		if (!checkOwnership(invoice)) {
			return false;
		}

		invoice.setState(state);
		invoiceDao.update(invoice);

		return true;
	}

	private boolean checkOwnership(Invoice invoice) {
		if (invoice != null && invoice.getEmployee() == null) {
			invoiceDao.loadAndLoadLazy(invoice);
		}

		Employee employeeLogged = securityLogic.getLoggedEmployee();

		// logged user is not owner
		if (!employeeLogged.getPk().equals(invoice.getEmployee().getPk())) {
			return false;
		}

		return true;
	}
}
