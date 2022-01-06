package cz.softinel.retra.invoiceseq.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.core.code.CodeGenerator;
import cz.softinel.retra.invoiceseq.InvoiceSeq;
import cz.softinel.retra.invoiceseq.dao.InvoiceSeqDao;
import cz.softinel.uaf.messages.Message;

/**
 * Implementation of invoice logic
 * 
 * @author Petr Sígl
 */
public class InvoiceSeqLogicImpl extends AbstractLogicBean implements InvoiceSeqLogic {

	private InvoiceSeqDao invoiceSeqDao;

	public void setInvoiceSeqDao(InvoiceSeqDao invoiceSeqDao) {
		this.invoiceSeqDao = invoiceSeqDao;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<InvoiceSeq> findAllActive() {
		return invoiceSeqDao.selectAllActive();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String getNextCodeForSequence(Long pk) {
		String result = getNextCodeForSequenceImpl(pk, false);
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String getNextCodeForSequenceIgnoreStep(Long pk) {
		String result = getNextCodeForSequenceImpl(pk, true);
		return result;
	}

	private String getNextCodeForSequenceImpl(Long pk, boolean ignoreStep) {
		String result = null;

		InvoiceSeq invoiceSeq = invoiceSeqDao.getForNextNumber(pk);
		// no sequence or sequence not active
		if (invoiceSeq == null || InvoiceSeq.STATE_ACTIVE != invoiceSeq.getState()) {
			addError(new Message("invoice.error.create.bad.sequence"));
			return null;
		}

		int step = 1;
		if (!ignoreStep) {
			step = invoiceSeq.getStep();
		}
		// generate code
		String pattern = invoiceSeq.getPattern();
		int newSeq = invoiceSeq.getSequence() + step;
		result = CodeGenerator.generateNewCode(pattern, newSeq);
		// update sequence
		invoiceSeq.setSequence(newSeq);

		return result;
	}

	public InvoiceSeq get(Long pk) {
		InvoiceSeq invoiceSeq = invoiceSeqDao.get(pk);
		return invoiceSeq;
	}
}
