package cz.softinel.retra.worklog.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.activity.blo.ActivityLogic;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.retra.spring.web.WizardFormController;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.blo.WorklogLogic;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;
import cz.softinel.uaf.messages.MessagesHolder;
import cz.softinel.uaf.spring.web.controller.HttpRequestContext;
import cz.softinel.uaf.spring.web.controller.HttpSessionContext;

/**
 * Wizard controller for Worklog Import use case.
 * 
 * @version $Revision: 1.7 $ $Date: 2007-11-26 18:50:53 $
 * @author Pavel Mueller
 */
public class WorklogImportController extends WizardFormController {

	private WorklogLogic worklogLogic;
	private ActivityLogic activityLogic;
	private ProjectLogic projectLogic;
	private InvoiceLogic invoiceLogic;

	private String successView;
	private String cancelView;

	/**
	 * Sets activity logic
	 * 
	 * @param activityLogic
	 *            the activityLogic to set
	 */
	public void setActivityLogic(ActivityLogic activityLogic) {
		this.activityLogic = activityLogic;
	}

	/**
	 * Sets project logic
	 * 
	 * @param projectLogic
	 *            the projectLogic to set
	 */
	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

	/**
	 * Sets worklog logic
	 * 
	 * @param worklogLogic
	 *            the worklogLogic to set
	 */
	public void setWorklogLogic(WorklogLogic worklogLogic) {
		this.worklogLogic = worklogLogic;
	}

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	/**
	 * Sets success view used at the end of this wizard.
	 * 
	 * @param successView
	 *            the successView to set
	 */
	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	/**
	 * Sets cancel view used if user presses Cancel.
	 * 
	 * @param cancelView
	 *            the cancelView to set
	 */
	public void setCancelView(String cancelView) {
		this.cancelView = cancelView;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new WorklogImportForm();
	}

	/**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// binding for uploaded file
		// binder.registerCustomEditor(String.class, new
		// StringMultipartFileEditor());
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors, int)
	 */
	@Override
	protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		WorklogImportForm form = (WorklogImportForm) command;
		if (page == 0) {
			if (form.isFreshImportData()) {
				// prepare data for mapping projects and activities
				List<Project> projects=projectLogic.findAllProjectsInWhichCouldDoWorkLog();
				int parseErrors = form.prepareMapping(getParser(form), projects,
						activityLogic.findAllNotDeletedActivities());
				/**
				 * if all lines in file are bad then there is no point in
				 * continuing with worklog import.
				 */
				if (form.isAllLinesBad()) {
					Messages messages = new Messages(getApplicationContext());
					messages.addError(new Message("worklogImportForm.parse.bad.file", new Object[] { parseErrors }));
					request.setAttribute(HttpRequestContext.REQUEST_MESSAGES_KEY, messages);
					request.setAttribute("badImportFile", "true");
					return;
				}
				if (parseErrors != 0) {
					// HACK pavel: to display a warn message without making a
					// controller superclass
					Messages messages = new Messages(getApplicationContext());
					messages.addWarning(new Message("worklogImportForm.parse.error", new Object[] { parseErrors }));
					request.setAttribute(HttpRequestContext.REQUEST_MESSAGES_KEY, messages);
				}
				if (getParser(form).storesDataToCookies()) {
					loadMappingFromCookies(request, form);
				}
			}
		} else if (page == 1) {
			// prepare worklog items to confirm in the last step
			List<Activity> activities = activityLogic.findAllNotDeletedActivities();
			List<Project> projects = projectLogic.findAllProjectsInWhichCouldDoWorkLog();
			List<Invoice> invoices = invoiceLogic.findAllInvoices();
			int parseErrors = form.prepareWorklogItems(getParser(form), projects, activities, invoices);
			if (parseErrors != 0) {
				// HACK pavel: to display a warn message without making a
				// controller superclass
				Messages messages = new Messages(getApplicationContext());
				messages.addWarning(new Message("worklogImportForm.parse.error", new Object[] { parseErrors }));
				request.setAttribute(HttpRequestContext.REQUEST_MESSAGES_KEY, messages);
			}
		}
	}

	/**
	 * Returns a data parser according to the import type
	 * 
	 * @param form
	 *            form with selected import type
	 * @return data parser
	 */
	private ImportDataParser getParser(WorklogImportForm form) {
		final String encoding = form.getImportDataEncoding();
		if (form.getImportType() == WorklogImportForm.ImportType.TYPE_CSV) {
			return new CsvParser(encoding);
		} else if (form.getImportType() == WorklogImportForm.ImportType.TYPE_TRACKIT) {
			return new TsvTrackItParser(encoding);
		} else if (form.getImportType() == WorklogImportForm.ImportType.TYPE_OUTLOOK_EXPRESS) {
			return new OutlookExpressParser(encoding);
		} else {
			throw new IllegalArgumentException("Unsupported import data type.");
		}
	}

	/**
	 * Loads project and activity mappings from cookies.
	 * 
	 * @param request
	 *            currect HTTP request
	 * @param form
	 *            current command object
	 */
	private void loadMappingFromCookies(HttpServletRequest request, WorklogImportForm form) {
		Cookie[] cookies = request.getCookies();
		ImportDataParser parser = getParser(form);
		String projectCookieName = parser.getProjectMappingCookieName();
		String activityCookieName = parser.getActivityMappingCookieName();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals(projectCookieName)) {
				form.setProjectMappingFromCookie(cookie.getValue());
			} else if (cookie.getName().equals(activityCookieName)) {
				form.setActivityMappingFromCookie(cookie.getValue());
			}
		}
	}

	/**
	 * Stores the last project and activity mapping into cookies.
	 * 
	 * @param response
	 *            current HTTP response
	 * @param form
	 *            current command object
	 */
	private void storeMappingToCookies(HttpServletResponse response, WorklogImportForm form) {
		// projects
		ImportDataParser parser = getParser(form);
		String projectCookieName = parser.getProjectMappingCookieName();
		if (projectCookieName != null) {
			Cookie cookie = new Cookie(projectCookieName, form.getProjectMappingCookie());
			cookie.setMaxAge(Integer.MAX_VALUE);
			response.addCookie(cookie);
		}

		// activities
		String activityCookieName = parser.getActivityMappingCookieName();
		if (activityCookieName != null) {
			Cookie cookie = new Cookie(activityCookieName, form.getActivityMappingCookie());
			cookie.setMaxAge(Integer.MAX_VALUE);
			response.addCookie(cookie);
		}
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      int)
	 */
	@Override
	protected Map referenceData(HttpServletRequest request, int page) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();

		// for the second page we need a list of projects and activities
		if (page == 1) {
			prepareActivities(model);
			prepareProjects(model);
			prepareInvoices(model);
		}

		return model;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object,
	 *      org.springframework.validation.Errors, int, boolean)
	 */
	@Override
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		if (!finish && page == 0) {
			// validation error if user did not upload a file
			ValidationUtils.rejectIfEmpty(errors, "importData", "worklogImportForm.file.required");
		}
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(cancelView);
	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		WorklogImportForm form = (WorklogImportForm) command;
		List<Worklog> worklogItems = form.getConfirmedWorklogItems();

		// find current user and set it into all confirmed worklog items
		Employee employee = getSecurityLogic().getLoggedEmployee();
		for (Worklog worklog : worklogItems) {
			worklog.setEmployee(employee);
			if (form.getSelectedInvoice() != null && form.getSelectedInvoice() > 0) {
				worklog.setInvoice(invoiceLogic.get(form.getSelectedInvoice()));
			}
		}

		// store to DB
		
		Messages messages = new Messages(getApplicationContext());
		MessagesHolder.setMessages(messages);
		int importItems = worklogLogic.create(worklogItems);

		// HACK pavel: to display a warn message without making a controller
		// superclass
		messages.addInfo(new Message("worklogImportForm.import.success", new Object[] { importItems }, 1));
		request.getSession().setAttribute(HttpSessionContext.SESSION_MESSAGES_KEY, messages);

		// set mapping into cookies
		if (getParser(form).storesDataToCookies()) {
			storeMappingToCookies(response, form);
		}

		return new ModelAndView(successView);
	}

	protected void prepareActivities(Map<String, Object> model) {
		prepareActivities(model, false);
	}

	protected void prepareActivities(Map<String, Object> model, boolean showAll) {
		List<Activity> activities = null;
		if (showAll) {
			activities = activityLogic.findAllActivities();
		} else {
			activities = activityLogic.findAllNotDeletedActivities();
		}
		model.put("activities", activities);
	}

	protected void prepareProjects(Map<String, Object> model) {
		List<Project> projects = null;
		projects = projectLogic.findAllActiveProjects();
		model.put("projects", projects);
	}

	protected void prepareInvoices(Map<String, Object> model) {
		List<Invoice> invoices = invoiceLogic.findAllActiveInvoicesForEmployee(getSecurityLogic().getLoggedEmployee().getPk());
		model.put("invoices", invoices);
		if (invoices.size() > 0) {
			model.put("showInvoices", Boolean.TRUE);
		}
	}
}
