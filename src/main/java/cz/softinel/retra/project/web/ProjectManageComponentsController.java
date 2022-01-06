package cz.softinel.retra.project.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.ProjectHelper;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ProjectManageComponentsController extends AbstractProjectFormController {

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		final ProjectForm projectForm = (ProjectForm) command;
		final String projectPkStr = requestContext.getParameter("pk");
		final Long projectPk = LongConvertor.getLongFromString(projectPkStr);
		Project project = new Project();
		project.setPk(projectPk);
		getProjectLogic().loadAndLoadLazy(project);
		ProjectHelper.entityToForm(project, projectForm);
	}

	// Configuration setter methods ..

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
	}

	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {
		return createModelAndView(getSuccessView());
	}

}
