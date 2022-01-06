package cz.softinel.retra.schedule.web;

import java.util.Date;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.IntegerConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.schedule.ScheduleHelper;
import cz.softinel.uaf.lovs.Lov;
import cz.softinel.uaf.lovs.LovsFactory;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ScheduleCopyController extends AbstractScheduleFormController {

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		ScheduleForm scheduleForm = (ScheduleForm) command;

		String schedulePkStr = requestContext.getParameter("schedulePk");
		Long schedulePk = LongConvertor.getLongFromString(schedulePkStr);
		if (schedulePk != null) {
			Schedule schedule = getScheduleLogic().get(schedulePk);
			ScheduleHelper.entityToForm(schedule, scheduleForm);
		}
	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		prepareTypes(model);
		prepareCopyTypes(model);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {
		int action = getAction();

		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			ScheduleForm scheduleForm = (ScheduleForm) command;
			// at first get all needed dates and copy type
			Date dateFrom = DateConvertor.getDateFromDateString(scheduleForm.getDate());
			Date destinationFrom = DateConvertor.getDateFromDateString(scheduleForm.getCopyDestinationFrom());
			Date destinationTo = DateConvertor.getDateFromDateString(scheduleForm.getCopyDestinationTo());
			Integer copyType = IntegerConvertor.getIntegerFromString(scheduleForm.getCopyType());
			// get current employee
			Employee employee = getSecurityLogic().getLoggedEmployee();

			if (copyType != null) {
				switch (copyType) {
				case ScheduleHelper.COPY_TYPE_DAY:
					getScheduleLogic().copyDay(employee, dateFrom, destinationFrom, destinationTo);
					break;
				case ScheduleHelper.COPY_TYPE_WEEK:
					getScheduleLogic().copyWeek(employee, dateFrom, destinationFrom, destinationTo);
				}
			}

			// some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put("scheduleForm", scheduleForm);
				prepareTypes(model);
				prepareCopyTypes(model);
				return createModelAndView(model, getFormView());
			} else {
				// add messages from bussines into our messages
				requestContext.convertMessagesToRedirectIgnoring();
			}
		}

		return createModelAndView(view);
	}

	protected void prepareCopyTypes(Model model) {
		LovsFactory factory = LovsFactory.getInstance();
		Lov lov = factory.getLov("scheduleCopyTypes", getApplicationContext());
		model.set("scheduleCopyTypes", lov.getFields());
	}
}
