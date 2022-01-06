package cz.softinel.retra.worklog.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class WorklogUpdateController extends AbstractWorklogFormController {

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		WorklogForm worklogForm = (WorklogForm) command;

		String worklogPkStr = requestContext.getParameter("worklogPk");
		Long worklogPk = LongConvertor.getLongFromString(worklogPkStr);
		if (worklogPk != null) {
			Worklog worklog = new Worklog();
			worklog.setPk(worklogPk);
			getWorklogLogic().loadAndLoadLazy(worklog);
			WorklogHelper.entityToForm(worklog, worklogForm);
		}
	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		prepareActivities(model, getShowHistoryData(requestContext));
		prepareProjects(model, getShowHistoryData(requestContext));
		prepareComponents(model, getShowHistoryData(requestContext));
		prepareInvoicesForCreateOrEdit(model);
		prepareJiraIssues(model);
		model.put("jiraIntegrationEnabled", this.isJiraIntegrationEnabled());
	}

	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {
		int action = getAction();

		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			WorklogForm worklogForm = (WorklogForm) command;
			// pk must be set, if not than it is real error
			Long worklogPk = LongConvertor.getLongFromString(worklogForm.getPk());
			if (worklogPk == null) {
				return createModelAndView(getErrorView());
			}
			// at first load old (for unsetable attributes from form, but not-null)
			Worklog worklog = new Worklog();
			worklog.setPk(worklogPk);
			getWorklogLogic().loadAndLoadLazy(worklog);
			// fill entity acording to form
			WorklogHelper.formToEntityUpdate(worklogForm, worklog);

			// get current employee
			Employee employee = getSecurityLogic().getLoggedEmployee();
			worklog.setEmployee(employee);
			if (worklog.hasAnyIssueTrackingWorklog()) {
				JiraWorklog jw = worklog.getCurrentIssueTrackingWorklog();
				jw.setEmployee(employee);
				if (jw.getPk() != null) {
					getJiraWorklogLogic().update(jw);
				} else {
					getJiraWorklogLogic().create(jw);
				}
			}

			getWorklogLogic().store(worklog);

			// some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put("worklogForm", worklogForm);
				prepareActivities(model, getShowHistoryData(requestContext));
				prepareProjects(model, getShowHistoryData(requestContext));
				prepareComponents(model, getShowHistoryData(requestContext));
				prepareInvoicesForCreateOrEdit(model);
				return createModelAndView(model, getFormView());
			}

			requestContext.addRedirectIgnoreInfo(new Message("worklog.updated"));
		}

		return createModelAndView(view);
	}
}
