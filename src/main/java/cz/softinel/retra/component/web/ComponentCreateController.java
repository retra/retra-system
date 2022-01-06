package cz.softinel.retra.component.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.component.Component;
import cz.softinel.retra.component.ComponentHelper;
import cz.softinel.retra.component.blo.ComponentLogic;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ComponentCreateController extends FormController {

	private ComponentLogic componentLogic;
	private ProjectLogic projectLogic;

	// Configuration setter methods ..

	public void setComponentLogic(ComponentLogic componentLogic) {
		this.componentLogic = componentLogic;
	}

	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		ComponentForm componentForm = (ComponentForm) command;
		String projectPkStr = requestContext.getParameter("projectPk");
		Long projectPk = LongConvertor.getLongFromString(projectPkStr);
		Component component = new Component();
		Project project = projectLogic.get(projectPk);
		component.setProject(project);
		ComponentHelper.entityToForm(component, componentForm);

	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
	}

	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {

		// Prepare form ...
		final ComponentForm form = (ComponentForm) command;
		final int action = getAction();
		final String view = getSuccessView();
		final String projectPk;

		if (action == ACTION_SAVE) {
			// Prepare entities ...
			Component component = new Component();
			// Map form into entities ...
			ComponentHelper.formToEntity(form, component);

			componentLogic.create(component);

			projectPk = component.getProject().getPk().toString();

			// Some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return new ModelAndView(view, "pk", projectPk);
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("projectManagement.createComponent.success"));
			}
		} else {
			// Cancel action ...
			projectPk = form.getProjectPk();
		}

		return new ModelAndView(view, "pk", projectPk);

	}

}
