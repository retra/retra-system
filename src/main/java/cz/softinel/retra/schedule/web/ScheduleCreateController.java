package cz.softinel.retra.schedule.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.schedule.ScheduleHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ScheduleCreateController extends AbstractScheduleFormController {

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		ScheduleForm scheduleForm = (ScheduleForm) errors.getTarget();
		prepareTypes(model);
		if (errors.getErrorCount() <= 0) {
			prepareScheduleForm(scheduleForm, requestContext);
		}
	}

	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {
		int action = getAction();

		String view = getSuccessView();
		if (action == ACTION_SAVE || action == ACTION_SAVE_AND_ADD) {
			ScheduleForm scheduleForm = (ScheduleForm) command;
			Schedule schedule = new Schedule();
			ScheduleHelper.formToEntity(scheduleForm, schedule);

			// get current employee
			Employee employee = getSecurityLogic().getLoggedEmployee();

			schedule.setEmployee(employee);
			getScheduleLogic().create(schedule);

			// some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), scheduleForm);
				prepareTypes(model);
				return createModelAndView(model, getFormView());
			}

			if (action == ACTION_SAVE_AND_ADD) {
				view = getSaveAndAddView();
				getCookieHelper().addToCookies(scheduleForm, requestContext);
			} else {
				getCookieHelper().addToCookies(scheduleForm, requestContext);
			}

			requestContext.addRedirectIgnoreInfo(new Message("schedule.created"));
		}

		return createModelAndView(view);
	}
}
