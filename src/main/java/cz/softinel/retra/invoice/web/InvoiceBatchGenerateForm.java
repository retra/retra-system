package cz.softinel.retra.invoice.web;

/**
 * Command object for the Invoice batch generate.
 * 
 * @author petr
 */
public class InvoiceBatchGenerateForm extends AbstractInvoiceForm {

	private Long[] confirmedItems;

	public Long[] getConfirmedItems() {
		return confirmedItems;
	}

	public void setConfirmedItems(Long[] confirmedItems) {
		this.confirmedItems = confirmedItems;
	}

}
