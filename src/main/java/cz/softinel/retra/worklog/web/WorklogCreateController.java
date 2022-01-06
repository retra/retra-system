package cz.softinel.retra.worklog.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class WorklogCreateController extends AbstractWorklogFormController {

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		WorklogForm worklogForm = (WorklogForm) errors.getTarget();
		prepareActivities(model, getShowHistoryData(requestContext));
		prepareProjects(model, getShowHistoryData(requestContext));
		prepareComponents(model, getShowHistoryData(requestContext));
		prepareInvoicesForCreateOrEdit(model);
		prepareJiraIssues(model);
		if (errors.getErrorCount() <= 0) {
			prepareWorklogForm(worklogForm, requestContext);
		}
		model.put("jiraIntegrationEnabled", this.isJiraIntegrationEnabled());
	}

	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {
		int action = getAction();

		String view = getSuccessView();
		if (action == ACTION_SAVE || action == ACTION_SAVE_AND_ADD) {
			WorklogForm worklogForm = (WorklogForm) command;
			Worklog worklog = new Worklog();
			WorklogHelper.formToEntity(worklogForm, worklog);

			Employee employee = getSecurityLogic().getLoggedEmployee();
			worklog.setEmployee(employee);
			if (worklog.hasAnyIssueTrackingWorklog()) {
				worklog.getCurrentIssueTrackingWorklog().setEmployee(employee);
			}

			getWorklogLogic().create(worklog);

			// some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), worklogForm);
				prepareActivities(model, getShowHistoryData(requestContext));
				prepareProjects(model, getShowHistoryData(requestContext));
				prepareComponents(model, getShowHistoryData(requestContext));
				prepareInvoicesForCreateOrEdit(model);

				return createModelAndView(model, getFormView());
			}

			if (action == ACTION_SAVE_AND_ADD) {
				view = getSaveAndAddView();
				Map helpParameters = new HashMap();
				helpParameters.put(WorklogHelper.HELP_COOKIE_PARAM_ADD_ALSO_FROM, Boolean.TRUE);
				getCookieHelper().addToCookies(worklogForm, requestContext, helpParameters);
			} else {
				getCookieHelper().addToCookies(worklogForm, requestContext);
			}

			requestContext.addRedirectIgnoreInfo(new Message("worklog.created"));
		}

		return createModelAndView(view);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.CommonFormController#doBeforeCancelAction(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	protected void doBeforeCancelAction(Model model, RequestContext requestContext, Object command,
			BindException errors) {
		getCookieHelper().cleanCookies(requestContext);
	}
}
