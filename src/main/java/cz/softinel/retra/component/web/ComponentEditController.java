package cz.softinel.retra.component.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.component.Component;
import cz.softinel.retra.component.ComponentHelper;
import cz.softinel.retra.component.blo.ComponentLogic;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ComponentEditController extends FormController {

	private ComponentLogic componentLogic;

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		ComponentForm componentForm = (ComponentForm) command;

		String componentPkStr = requestContext.getParameter("pk");
		Long componentPk = LongConvertor.getLongFromString(componentPkStr);
		Component component = componentLogic.get(componentPk);
		ComponentHelper.entityToForm(component, componentForm);

	}

	// Configuration setter methods ..

	public void setComponentLogic(ComponentLogic componentLogic) {
		this.componentLogic = componentLogic;
	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
	}

	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {

		int action = getAction();
		String view = getSuccessView();
		String projectPk = null;
		if (action == ACTION_SAVE) {
			// Prepare form ...
			ComponentForm form = (ComponentForm) command;
			// Prepare entities ...
			Component component = componentLogic.get(Long.valueOf(form.getPk()));
			// Map form into entities ...
			ComponentHelper.formToEntity(form, component);

			componentLogic.store(component);

			projectPk = form.getProjectPk();

			// Some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return new ModelAndView(view, "pk", projectPk);
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("projectManagement.editComponent.success"));
			}

		} else if (action == ACTION_UNDEFINED) {
			// This is used for the Cancel button, because this should return not only
			// cancelView, but view with parameter
			projectPk = ((ComponentForm) command).getProjectPk();
		}

		return new ModelAndView(view, "pk", projectPk);

	}

}
