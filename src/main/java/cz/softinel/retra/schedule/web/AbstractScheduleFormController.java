package cz.softinel.retra.schedule.web;

import java.util.List;

import cz.softinel.retra.schedule.blo.ScheduleLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.retra.type.Type;
import cz.softinel.retra.type.blo.TypeLogic;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public abstract class AbstractScheduleFormController extends FormController {

	private ScheduleLogic scheduleLogic;
	private TypeLogic typeLogic;

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

	protected void prepareScheduleForm(ScheduleForm scheduleForm, RequestContext requestContext) {
		getCookieHelper().importFromCookies(scheduleForm, requestContext);
	}

	protected void prepareTypes(Model model) {
		List<Type> types = typeLogic.findAllTypes();
		model.put("types", types);
	}
}
