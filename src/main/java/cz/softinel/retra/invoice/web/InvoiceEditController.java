package cz.softinel.retra.invoice.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.InvoiceHelper;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class InvoiceEditController extends AbstractInvoiceFormController {

	private InvoiceLogic invoiceLogic;

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		InvoiceForm invoiceForm = (InvoiceForm) command;

		String invoicePkStr = requestContext.getParameter("pk");
		Long invoicePk = LongConvertor.getLongFromString(invoicePkStr);

		Invoice invoice = invoiceLogic.get(invoicePk);
		InvoiceHelper.entityToForm(invoice, invoiceForm);
	}

	// Configuration setter methods ..

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		model.put("isCodeGenerated", invoiceLogic.isCodeGenerated());
		super.showForm(model, requestContext, errors);
	}

	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {

		int action = getAction();
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			// Prepare form ...
			InvoiceForm form = (InvoiceForm) command;
			// Prepare entities ...
			Long pk = LongConvertor.getLongFromString(form.getPk());
			Invoice invoice = invoiceLogic.get(pk);
			// Map form into entities ...
			InvoiceHelper.formToEntity(form, invoice, invoiceLogic.isCodeGenerated());

			invoiceLogic.store(invoice);
			// Some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("invoice.edit.success"));
			}
		}
		return createModelAndView(view);
	}

}
