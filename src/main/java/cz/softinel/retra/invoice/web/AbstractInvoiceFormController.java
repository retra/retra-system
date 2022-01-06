package cz.softinel.retra.invoice.web;

import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public abstract class AbstractInvoiceFormController extends FormController {

	private InvoiceLogic invoiceLogic;

	/**
	 * @return the invoiceLogic
	 */
	public InvoiceLogic getInvoiceLogic() {
		return invoiceLogic;
	}

	/**
	 * @param invoiceLogic the invoiceLogic to set
	 */
	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	protected void prepareInvoiceForm(InvoiceForm invoiceForm, RequestContext requestContext) {
		getCookieHelper().importFromCookies(invoiceForm, requestContext);
	}
}
