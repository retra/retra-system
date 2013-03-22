package cz.softinel.retra.schedule.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.schedule.ScheduleHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ScheduleUpdateController extends AbstractScheduleFormController {

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		ScheduleForm scheduleForm = (ScheduleForm) command;
		
		String schedulePkStr = requestContext.getParameter("schedulePk");
		Long schedulePk = LongConvertor.getLongFromString(schedulePkStr);
		if (schedulePk != null){
			Schedule schedule = getScheduleLogic().get(schedulePk);
			ScheduleHelper.entityToForm(schedule, scheduleForm);
		}
	}
	
	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		prepareTypes(model);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors) throws Exception {
		int action = getAction();
		
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			ScheduleForm scheduleForm = (ScheduleForm) command;
			//pk must be set, if not than it is real error
			Long schedulePk = LongConvertor.getLongFromString(scheduleForm.getPk());
			if (schedulePk == null){
				return createModelAndView(getErrorView());
			}
			//at first load old (for unsetable attributes from form, but not-null)
			Schedule schedule = getScheduleLogic().get(schedulePk);
			//fill entity acording to form
			ScheduleHelper.formToEntity(scheduleForm, schedule);

			//get current employee
			Employee employee = getSecurityLogic().getLoggedEmployee();
	
			schedule.setEmployee(employee);
			getScheduleLogic().store(schedule);
			
			//some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put("scheduleForm", scheduleForm);
				prepareTypes(model);
				return createModelAndView(model, getFormView());
			}
			
			requestContext.addRedirectIgnoreInfo(new Message("schedule.updated"));
		}

		return createModelAndView(view);
	}
}
