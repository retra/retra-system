package cz.softinel.retra.invoice.web;

import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.InvoiceHelper;
import cz.softinel.retra.invoiceseq.InvoiceSeq;
import cz.softinel.retra.invoiceseq.blo.InvoiceSeqLogic;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class InvoiceCreateController extends AbstractInvoiceFormController {

	private InvoiceSeqLogic invoiceSeqLogic;
	
	// Configuration setter methods ..
	
	public void setInvoiceSeqLogic(InvoiceSeqLogic invoiceSeqLogic) {
		this.invoiceSeqLogic = invoiceSeqLogic;
	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		model.put("isCodeGenerated", getInvoiceLogic().isCodeGenerated());
		prepareSequences(model);

		InvoiceForm invoiceForm = (InvoiceForm)errors.getTarget();
		if (errors.getErrorCount() <= 0) {
			prepareInvoiceForm(invoiceForm, requestContext);
		}
		super.showForm(model, requestContext, errors);
	}	
	
	
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors) throws Exception {
		int action = getAction();
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			// Prepare form ...
			InvoiceForm form = (InvoiceForm) command;
			// Prepare entities ...
			Invoice invoice = new Invoice();
			// Map form into entities ...
			InvoiceHelper.formToEntity(form, invoice, getInvoiceLogic().isCodeGenerated());

			Employee employee = getSecurityLogic().getLoggedEmployee();
			invoice.setEmployee(employee);
			
			try{
				Long sequencePk = LongConvertor.getLongFromString(form.getSequence());
				getInvoiceLogic().create(invoice, sequencePk);
			} catch(Exception le){
				requestContext.addError(new Message("invoice.create.invoice.exists"));
			}
			
			//some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				model.put("isCodeGenerated", getInvoiceLogic().isCodeGenerated());
				prepareSequences(model);
				return createModelAndView(model, getFormView());
			}

			getCookieHelper().addToCookies(form, requestContext);
			requestContext.addRedirectIgnoreInfo(new Message("invoice.create.success"));
		}

		return createModelAndView(view);
	}	

	private void prepareSequences(Model model) {
		List<InvoiceSeq> sequences = invoiceSeqLogic.findAllActive();
		model.put("sequences", sequences);
	}
}
