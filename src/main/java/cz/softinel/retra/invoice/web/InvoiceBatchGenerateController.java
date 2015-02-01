package cz.softinel.retra.invoice.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.core.utils.validator.CommonValidator;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.invoiceseq.InvoiceSeq;
import cz.softinel.retra.invoiceseq.blo.InvoiceSeqLogic;
import cz.softinel.retra.spring.web.WizardFormController;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;
import cz.softinel.uaf.spring.web.controller.HttpRequestContext;
import cz.softinel.uaf.spring.web.controller.HttpSessionContext;

/**
 * Invoice batch generate.
 * 
 * @author petr
 *
 */
public class InvoiceBatchGenerateController extends WizardFormController {
	
	private InvoiceLogic invoiceLogic;
	private InvoiceSeqLogic invoiceSeqLogic;
	private EmployeeLogic employeeLogic;
	
	private InvoiceCookieHelper cookieHelper;
	
	private String successView;
	private String cancelView;

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	public void setInvoiceSeqLogic(InvoiceSeqLogic invoiceSeqLogic) {
		this.invoiceSeqLogic = invoiceSeqLogic;
	}

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	public void setCookieHelper(InvoiceCookieHelper cookieHelper) {
		this.cookieHelper = cookieHelper;
	}

	/**
	 * Sets success view used at the end of this wizard.
	 * @param successView the successView to set
	 */
	public void setSuccessView(String successView) {
		this.successView = successView;
	}
	
	/**
	 * Sets cancel view used if user presses Cancel.
	 * @param cancelView the cancelView to set
	 */
	public void setCancelView(String cancelView) {
		this.cancelView = cancelView;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new InvoiceBatchGenerateForm();
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@Override
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		InvoiceBatchGenerateForm form = (InvoiceBatchGenerateForm) command;
		if (page == 0) {
			if (!errors.hasErrors()) {
				//prepare invoice seq
				Long pk = LongConvertor.getLongFromString(form.getSequence());
				InvoiceSeq selectedInvoiceSeq = invoiceSeqLogic.get(pk);
				request.setAttribute("selectedInvoiceSeq", selectedInvoiceSeq);
	
				//prepare employees to confirm
				List<Employee> employees = employeeLogic.getAllEmployeesForGeneratingInvoice();
				request.setAttribute("employeesForBatchGenerate", employees);
			}
		} else if (page == 1) {
			//nothing?
			form.setConfirmedItems(new Long[]{});
		}
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, int page) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		// first page - selects
		if (page == 0) {
			model.put("isCodeGenerated", invoiceLogic.isCodeGenerated());
			//prepare seq
			prepareSequences(model);
		} else if (page == 1) {
			//invoice seq
			InvoiceSeq invoiceSeq = (InvoiceSeq) request.getAttribute("selectedInvoiceSeq");
			model.put("invoiceSeq", invoiceSeq);
			//employees
			List<Employee> employees = (List<Employee>) request.getAttribute("employeesForBatchGenerate");
			model.put("employees", employees);
		}
		
		return model;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	@Override
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		if (page == 0) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sequence", "invoice.error.require.sequence");

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderDate", "invoice.error.require.orderDate");
			CommonValidator.validateDate("orderDate", errors, "invoice.error.bad.format.orderDate", null);

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "finishDate", "invoice.error.require.finishDate");
			CommonValidator.validateDate("finishDate", errors, "invoice.error.bad.format.finishDate", null);

			validateFinishGreaterThanOrder(errors);
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "invoice.error.require.name");
		}
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(cancelView);
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		InvoiceBatchGenerateForm form = (InvoiceBatchGenerateForm) command;
		Long invoiceSeqPk = LongConvertor.getLongFromString(form.getSequence());
		String name = form.getName();
		Date orderDate = DateConvertor.getDateFromDateString(form.getOrderDate());
		Date finishDate = DateConvertor.getDateFromDateString(form.getFinishDate());
		Long[] employeePks = form.getConfirmedItems();
		
		// store to DB
		List<Invoice> generatedInvoices = invoiceLogic.batchCreate(invoiceSeqPk, name, orderDate, finishDate, employeePks);
		
		// HACK: to display a warn message without making a controller superclass
		Messages messages = new Messages(getApplicationContext());
		messages.addInfo(new Message("invoiceBatchGenerate.success", new Object[] { generatedInvoices.size() }, 1));
		request.getSession().setAttribute(HttpSessionContext.SESSION_MESSAGES_KEY, messages);
		request.getSession().setAttribute("invoiceBatchResult", generatedInvoices);
	
		return new ModelAndView(successView);
	}

	private void prepareSequences(Map<String, Object> model) {
		List<InvoiceSeq> sequences = invoiceSeqLogic.findAllActive();
		model.put("sequences", sequences);
	}
	
	protected void prepareInvoicesForCreateOrEdit(Map<String, Object> model) {
		List<Invoice> invoices = invoiceLogic.findAllActiveInvoicesForEmployee(getSecurityLogic().getLoggedEmployee().getPk());
		model.put("invoices", invoices);
	}

	private void validateFinishGreaterThanOrder(Errors errors){
		Date order = null;
		Date finish = null;
		try {
			String orders = (String)errors.getFieldValue("orderDate");
			String finishs = (String)errors.getFieldValue("finishDate");

			order = DateConvertor.convertToDateFromDateString(orders);
			finish = DateConvertor.convertToDateFromDateString(finishs);
		}
		catch (ConvertException e) {
			//couldn't compare return
			return;
		}
		
		if (order.getTime() >= finish.getTime()) {
			errors.reject("invoiceForm.finish.greater.order");
		}
	}

}
