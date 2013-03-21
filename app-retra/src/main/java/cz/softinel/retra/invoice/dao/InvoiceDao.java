package cz.softinel.retra.invoice.dao;

import java.util.List;

import cz.softinel.retra.invoice.Invoice;
import cz.softinel.uaf.filter.Filter;

/**
 * Dao for invoice
 *
 * @author Petr Sígl
 */
public interface InvoiceDao {

	/**
	 * Returns invoice according to primary key. 
	 * 
	 * @param pk primary key of invoice
	 * @return
	 */
	public Invoice get(Long pk);
	
	/**
	 * Insert Invoice
	 * 
	 * @param invoice to insert
	 */
	public Invoice insert(Invoice invoice);

	/**
	 * Update invoice
	 * 
	 * @param invoice to update
	 */
	public void update(Invoice invoice);
	
	/**
	 * Returns all invoices
	 * 
	 * @return all invoices
	 */
	public List<Invoice> selectAll();

	/**
	 * Returns all invoices for employee
	 * 
	 * @return all invoices
	 */
	public List<Invoice> selectAllForEmployee(Long employeePk);
	
	/**
	 * Returns all invoices for employee which are not deleted
	 * 
	 * @return all invoices
	 */
	public List<Invoice> selectAllForEmployeeNotDeleted(Long employeePk);

	/**
	 * Returns all active invoices for employee.
	 * 
	 * @return all invoices
	 */
	public List<Invoice> selectAllForEmployeeActive(Long employeePk);
	
	/**
	 * @return invoices with the given code
	 */
	public List<Invoice> selectInvoicesForEmployeeWithCode(Long employeePk, String code);
	
	/**
	 * Select by filter.
	 * 
	 * @param filter
	 * @return
	 */
	public List<Invoice> selectByFilter(Filter filter);
	
	/**
	 * Load informations about invoice (defined by pk)
	 * 
	 * @param invoice where load and where is pk set
	 */
	public void load(Invoice invoice);
	
}
