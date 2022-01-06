package cz.softinel.retra.invoiceseq.blo;

import java.util.List;

import cz.softinel.retra.invoiceseq.InvoiceSeq;

/**
 * This class represents all logic for invoice sequences.
 *
 * @author Petr Sígl
 */
public interface InvoiceSeqLogic {

	/**
	 * Find all invoice items, which are active.
	 * 
	 * @return
	 */
	public List<InvoiceSeq> findAllActive();

	/**
	 * Returns next code in sequence and update!!! sequence in DB.
	 * 
	 * @param pk
	 * @return
	 */
	public String getNextCodeForSequence(Long pk);

	/**
	 * Returns next code in sequence and update!!! sequence in DB.
	 * 
	 * @param pk
	 * @return
	 */
	public String getNextCodeForSequenceIgnoreStep(Long pk);

	/**
	 * Returns sequence in DB.
	 * 
	 * @param pk
	 * @return
	 */
	public InvoiceSeq get(Long pk);

}
