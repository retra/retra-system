package cz.softinel.retra.worklog.web;

import java.util.ArrayList;
import java.util.Calendar;
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

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.activity.blo.ActivityLogic;
import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.core.utils.validator.CommonValidator;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.retra.spring.web.WizardFormController;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.blo.WorklogLogic;
import cz.softinel.retra.worklog.dao.WorklogFilter;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;
import cz.softinel.uaf.spring.web.controller.HttpSessionContext;

/**
 * Pair worklog and invoice.
 * 
 * @author petr
 *
 */
public class WorklogInvoicePairController extends WizardFormController {
	
	private WorklogLogic worklogLogic;
	private ActivityLogic activityLogic;
	private ProjectLogic projectLogic;
	private InvoiceLogic invoiceLogic;
	
	private String successView;
	private String cancelView;
	
	/**
	 * Sets activity logic
	 * @param activityLogic the activityLogic to set
	 */
	public void setActivityLogic(ActivityLogic activityLogic) {
		this.activityLogic = activityLogic;
	}

	/**
	 * Sets project logic
	 * @param projectLogic the projectLogic to set
	 */
	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

	/**
	 * Sets worklog logic
	 * @param worklogLogic the worklogLogic to set
	 */
	public void setWorklogLogic(WorklogLogic worklogLogic) {
		this.worklogLogic = worklogLogic;
	}

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
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
		return new WorklogInvoicePairForm();
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@Override
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		WorklogInvoicePairForm form = (WorklogInvoicePairForm) command;
		if (page == 0) {
			if (!errors.hasErrors()) {
				//prepare invoice
				Long pk = LongConvertor.getLongFromString(form.getInvoice());
				Invoice selectedInvoice = invoiceLogic.get(pk);
				request.setAttribute("selectedInvoiceForPair", selectedInvoice);
	
				//prepare worklog items to confirm
				Filter filter = prepareWorklogFilter(form);
				List<Worklog> worklogs = worklogLogic.findByFilter(filter);
				request.setAttribute("worklogItemsNotOnInvoiceForEmployee", worklogs);
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
			prepareActivities(model, getShowHistoryData(request));
			prepareProjects(model, getShowHistoryData(request));
			prepareInvoicesForCreateOrEdit(model);
		} else if (page == 1) {
			Invoice invoice = (Invoice) request.getAttribute("selectedInvoiceForPair");
			model.put("invoice", invoice);
			List<Worklog> worklogs = (List<Worklog>) request.getAttribute("worklogItemsNotOnInvoiceForEmployee");
			model.put("worklogs", worklogs);
		}
		
		return model;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int, boolean)
	 */
	@Override
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		if (page == 0) {
			ValidationUtils.rejectIfEmpty(errors, "invoice", "worklogInvoicePair.invoice.required");

			CommonValidator.validateDate("dateFrom", errors, "worklogInvoicePair.dateFrom.bad.format", null);
			CommonValidator.validateDate("dateTo", errors, "worklogInvoicePair.dateTo.bad.format", null);
		
			validateToGreaterThanFrom(errors);
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
		WorklogInvoicePairForm form = (WorklogInvoicePairForm) command;
		Long invoicePk = LongConvertor.getLongFromString(form.getInvoice());
		Long[] worklogItemPks = form.getConfirmedItems();
		
		// store to DB
		int pairedItems = worklogLogic.pairWorklogWithInvoice(invoicePk, worklogItemPks);
		
		// HACK: to display a warn message without making a controller superclass
		Messages messages = new Messages(getApplicationContext());
		messages.addInfo(new Message("worklogInvoicePair.success", new Object[] { pairedItems }, 1));
		request.getSession().setAttribute(HttpSessionContext.SESSION_MESSAGES_KEY, messages);
	
		return new ModelAndView(successView);
	}

	protected void prepareActivities(Map<String, Object> model) {
		prepareActivities(model, false);
	}

	protected void prepareActivities(Map<String, Object> model, boolean showAll) {
		List<Activity> activities = null;
		if (showAll) {
			activities = activityLogic.findAllActivities();
		}
		else {
			activities = activityLogic.findAllNotDeletedActivities();			
		}
		model.put("activities", activities);
	}

	protected void prepareProjects(Map<String, Object> model) {
		prepareProjects(model, false);
	}

	protected void prepareProjects(Map<String, Object> model, boolean showAll) {
		List<Project> projects = null;
		if (showAll) {
			//projects = projectLogic.findAllProjects();
			projects = new ArrayList<Project>(getSecurityLogic().getLoggedEmployee().getProjects());
		}
		else {
			//projects = projectLogic.findAllNotDeletedProjects();	
			projects = new ArrayList<Project>(getSecurityLogic().getLoggedEmployee().getProjects());
		}
		model.put("projects", projects);
	}

	protected void prepareInvoicesForCreateOrEdit(Map<String, Object> model) {
		List<Invoice> invoices = invoiceLogic.findAllActiveInvoicesForEmployee(getSecurityLogic().getLoggedEmployee().getPk());
		model.put("invoices", invoices);
	}

	private void validateToGreaterThanFrom(Errors errors){
		//FUJ: this is not realy nice, but it works
		String date = DateConvertor.convertToDateStringFromDate(new Date());
		String first = (String)errors.getFieldValue("dateTo");
		String second = (String)errors.getFieldValue("dateFrom");
		
		if (CommonValidator.isValidHour(first) && CommonValidator.isValidHour(second)){
			Date to = null;
			Date from = null;
			try {
				to = DateConvertor.convertToDateFromDateStringHourString(date, first);
				from = DateConvertor.convertToDateFromDateStringHourString(date, second);
			}
			catch (ConvertException e) {
				//couldn't compare return
				return;
			}
			
			if (from.getTime() >= to.getTime()) {
				errors.reject("worklogInvoicePair.dateTo.greater.dateForm");
			}
		}
	}

	private Filter prepareWorklogFilter(WorklogInvoicePairForm form) {
		Filter newFilter = new WorklogFilter();

		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_FROM, form.getDateFrom(), newFilter);
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_TO, form.getDateTo(), newFilter);
		
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_PROJECT, form.getProject(), newFilter);
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_ACTIVITY, form.getActivity(), newFilter);

		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_EMPLOYEE, getSecurityLogic().getLoggedEmployee().getPk(), newFilter);
		FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_ON_INVOICE, Boolean.FALSE, newFilter);
		
		return newFilter;
	}

}
