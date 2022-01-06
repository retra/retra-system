package cz.softinel.retra.invoice.blo;

import java.util.Date;
import java.util.List;

import cz.softinel.retra.invoice.Invoice;
import cz.softinel.uaf.filter.Filter;

/**
 * This class represents all logic for invoices.
 *
 * @author Petr Sígl
 */
public interface InvoiceLogic {

	/**
	 * Returns true if code for invoice is generated from DB or not.
	 * 
	 * @return
	 */
	public boolean isCodeGenerated();

	/**
	 * Find all invoice items.
	 * 
	 * @return
	 */
	public List<Invoice> findAllInvoices();

	/**
	 * Find all invoice items for employee.
	 * 
	 * @return
	 */
	public List<Invoice> findAllInvoicesForEmployee(Long employeePk);

	/**
	 * Find all invoice items for employee, which are not deleted.
	 * 
	 * @return
	 */
	public List<Invoice> findAllNotDeletedInvoicesForEmployee(Long employeePk);

	/**
	 * Find all active invoice items for employee.
	 * 
	 * @return
	 */
	public List<Invoice> findAllActiveInvoicesForEmployee(Long employeePk);

	/**
	 * Find all invoice items for employee, with code.
	 * 
	 * @param code
	 * @return
	 */
	public List<Invoice> findInvoicesForEmployeeWithCode(Long employeePk, String code);

	/**
	 * Find invoices by filter.
	 * 
	 * @param filter
	 * @return
	 */
	public List<Invoice> findByFilter(Filter filter);

	/**
	 * Create invoice,
	 * 
	 * @param invoice
	 * @return
	 */
	public Invoice create(Invoice invoice);

	/**
	 * Create invoice,
	 * 
	 * @param invoice
	 * @param sequencePk
	 * @return
	 */
	public Invoice create(Invoice invoice, Long sequencePk);

	/**
	 * Batch create invoices.
	 * 
	 * @param sequencePk
	 * @param name
	 * @param orderDate
	 * @param finishDate
	 * @param employeePks
	 * @return
	 */
	public List<Invoice> batchCreate(Long sequencePk, String name, Date orderDate, Date finishDate, Long[] employeePks);

	/**
	 * Get invoice according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public Invoice get(Long pk);

	/**
	 * Stores given Invoice
	 * 
	 * @param Invoice
	 */
	public void store(Invoice invoice);

	/**
	 * Remove given Invoice
	 * 
	 * @param Invoice
	 */
	public void remove(Invoice invoice);

	/**
	 * Close given Invoice
	 * 
	 * @param Invoice
	 */
	public void close(Invoice invoice);

	/**
	 * Open given Invoice
	 * 
	 * @param Invoice
	 */
	public void open(Invoice invoice);

}
