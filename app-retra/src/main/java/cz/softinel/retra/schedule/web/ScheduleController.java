package cz.softinel.retra.schedule.web;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.core.utils.helper.DateHelper;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.schedule.ScheduleWeekOverview;
import cz.softinel.retra.schedule.blo.ScheduleLogic;
import cz.softinel.retra.schedule.dao.ScheduleFilter;
import cz.softinel.retra.spring.web.DispatchController;
import cz.softinel.retra.type.Type;
import cz.softinel.retra.type.blo.TypeLogic;
import cz.softinel.retra.worklog.dao.WorklogFilter;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.lovs.Lov;
import cz.softinel.uaf.lovs.LovsFactory;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * Controller for schedule
 * 
 * @version $Revision: 1.7 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public class ScheduleController extends DispatchController {

	private ScheduleLogic scheduleLogic;
	private EmployeeLogic employeeLogic;
	private TypeLogic typeLogic;

	/**
	 * @return the employeeLogic
	 */
	public EmployeeLogic getEmployeeLogic() {
		return employeeLogic;
	}

	/**
	 * @param employeeLogic the employeeLogic to set
	 */
	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	/**
	 * @return the scheduleLogic
	 */
	public ScheduleLogic getScheduleLogic() {
		return scheduleLogic;
	}

	/**
	 * @param scheduleLogic the scheduleLogic to set
	 */
	public void setScheduleLogic(ScheduleLogic scheduleLogic) {
		this.scheduleLogic = scheduleLogic;
	}

	/**
	 * @return the typeLogic
	 */
	public TypeLogic getTypeLogic() {
		return typeLogic;
	}

	/**
	 * @param typeLogic the typeLogic to set
	 */
	public void setTypeLogic(TypeLogic typeLogic) {
		this.typeLogic = typeLogic;
	}

	@Override
	public Filter getFilter(Model model) {
		Filter filter = (Filter) model.get(ScheduleFilter.class.toString());
		if (filter == null) {
			filter = new ScheduleFilter();
			model.set(ScheduleFilter.class.toString(), filter);
		}
		return filter;		
	}

	/**
	 * Show schedule for user.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView scheduleList(Model model, RequestContext requestContext) {
		Filter filter = getFilter(model);
		Long employeePk = FilterHelper.getFieldAsLong(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, filter);
		String fromStr = FilterHelper.getFieldAsString(ScheduleFilter.SCHEDULE_FILTER_FROM, filter);
		String toStr = FilterHelper.getFieldAsString(ScheduleFilter.SCHEDULE_FILTER_TO, filter);

		// employee not set, so show actualy logged user
		if (employeePk == null) {
			employeePk = getSecurityLogic().getLoggedEmployee().getPk();
			FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, employeePk, filter);
		}
		if (fromStr == null) {
			Date from = DateHelper.getStartOfMonth(new Date());
			FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_FROM, from, true, filter);
		}
		if (toStr == null) {
			Date to = DateHelper.getEndOfMonth(new Date());
			FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_TO, to, true, filter);
		}

		prepareTypes(model);
		prepareEmployees(model);
		prepareEmployee(model, employeePk);
		prepareStates(model);

		List<Schedule> schedules = scheduleLogic.findByFilter(filter);
		model.set("schedules", schedules);

		return createModelAndView(model, getSuccessView());
	}

	/**
	 * View given schedule.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView scheduleView(Model model, RequestContext requestContext) {
		String schedulePkStr = requestContext.getParameter("schedulePk");
		Long schedulePk = LongConvertor.getLongFromString(schedulePkStr);

		String view = getSuccessView();
		if (schedulePk != null) {
			Schedule schedule = scheduleLogic.get(schedulePk);
			model.put("schedule", schedule);
		} else {
			view = getErrorView();
		}

		return createModelAndView(model, view);
	}

	/**
	 * Delete given schedule for user.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView scheduleDelete(Model model, RequestContext requestContext) {
		String schedulePkStr = requestContext.getParameter("schedulePk");
		Long schedulePk = LongConvertor.getLongFromString(schedulePkStr);
		String view = getSuccessView();
		if (schedulePk != null) {
			scheduleLogic.remove(schedulePk);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView();
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("schedule.deleted"));
			}
			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView();
		}

		return createModelAndView(model, view);
	}

	public ModelAndView scheduleOverviewList(Model model, RequestContext requestContext) {
		Filter filter = getFilter(model);
		Long employeePk = FilterHelper.getFieldAsLong(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, filter);
		Integer year = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_YEAR, filter);
		Integer month = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);

		// employee not set, so show actualy logged user
		if (employeePk == null) {
			employeePk = getSecurityLogic().getLoggedEmployee().getPk();
			FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_EMPLOYEE, employeePk, filter);
		}
		// year not set
		if (year == null) {
			Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_YEAR, year, filter);
		}
		// month not set
		if (month == null) {
			Calendar calendar = Calendar.getInstance();
			month = calendar.get(Calendar.MONTH);
			FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_MONTH, month, filter);
		}

		prepareYears(model);
		prepareMonths(model);
		prepareEmployees(model);
		prepareTypes(model);
		prepareEmployee(model, employeePk);

		// prepare next and previous parameters
		prepareNext(model, filter);
		preparePrevious(model, filter);

		List<ScheduleWeekOverview> schedules = scheduleLogic.findWeekOverviews(filter);
		model.set("schedules", schedules);

		return createModelAndView(model, getSuccessView());
	}

	protected void prepareTypes(Model model) {
		List<Type> types = typeLogic.findAllTypes();
		model.set("types", types);
	}

	protected void prepareEmployees(Model model) {
		List<Employee> employees = employeeLogic.getAllEmployeesNotFull();
		model.set("employees", employees);
	}

	protected void prepareEmployee(Model model, Long employeePk) {
		Employee employee = new Employee();
		employee.setPk(employeePk);
		employeeLogic.load(employee);
		model.set("employee", employee);
	}

	protected void prepareMonths(Model model) {
		LovsFactory factory = LovsFactory.getInstance();
		Lov lov = factory.getLov("months", getApplicationContext());
		model.set("months", lov.getFields());
	}

	protected void prepareYears(Model model) {
		LovsFactory factory = LovsFactory.getInstance();
		Lov lov = factory.getLov("years", getApplicationContext());
		model.set("years", lov.getFields());
	}

	protected void prepareStates(Model model) {
		LovsFactory factory = LovsFactory.getInstance();
		Lov lov = factory.getLov("scheduleStates", getApplicationContext());
		model.set("scheduleStates", lov.getFields());
	}

	protected void prepareNext(Model model, Filter filter) {
		Filter newFilter = prepareNewFilter(filter, 1);
		super.prepareNext(model, newFilter);
	}

	protected void preparePrevious(Model model, Filter filter) {
		Filter newFilter = prepareNewFilter(filter, -1);
		super.preparePrevious(model, newFilter);
	}

	private Filter prepareNewFilter(Filter filter, int addMonth) {
		Filter newFilter = new ScheduleFilter();
		for (String fieldName : filter.getAllFields()) {
			newFilter.setFieldValue(fieldName, filter.getFieldValue(fieldName));
		}
		Integer year = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_YEAR, filter);
		Integer month = FilterHelper.getFieldAsInteger(ScheduleFilter.SCHEDULE_FILTER_MONTH, filter);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, 1);
		calendar.add(Calendar.MONTH, addMonth);
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_YEAR, year, newFilter);
		FilterHelper.setField(ScheduleFilter.SCHEDULE_FILTER_MONTH, month, newFilter);
		return newFilter;
	}
}
