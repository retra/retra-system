package cz.softinel.retra.invoiceseq.dao;

import java.util.List;

import cz.softinel.retra.invoiceseq.InvoiceSeq;

/**
 * Dao for invoice sequence
 *
 * @author Petr Sígl
 */
public interface InvoiceSeqDao {

	/**
	 * Returns invoice sequence according to primary key.
	 * 
	 * @param pk primary key of invoice
	 * @return
	 */
	public InvoiceSeq get(Long pk);

	/**
	 * Returns invoice sequence according to primary key.
	 * 
	 * @param pk primary key of invoice
	 * @return
	 */
	public InvoiceSeq getForNextNumber(Long pk);

	/**
	 * Update invoice
	 * 
	 * @param invoice to update
	 */
	public void update(InvoiceSeq invoice);

	/**
	 * Returns all invoices which are active
	 * 
	 * @return all invoices
	 */
	public List<InvoiceSeq> selectAllActive();

}
