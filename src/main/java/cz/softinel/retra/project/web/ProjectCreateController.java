package cz.softinel.retra.project.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.ProjectHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ProjectCreateController extends AbstractProjectFormController {
	
	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
		prepareProjects(model);
		prepareCategories(model);
		
		if (errors.getErrorCount() <= 0) {
			ProjectForm projectForm = (ProjectForm)errors.getTarget();
			projectForm.setWorkEnabled(Boolean.TRUE);
		}
	}	
	
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors) throws Exception {
		int action = getAction();
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			// Prepare form ...
			ProjectForm form = (ProjectForm) command;
			// Prepare entities ...
			Project project = new Project();
			// Map form into entities ...
			ProjectHelper.formToEntity(form, project);

			try{
				getProjectLogic().create(project);

				//add me to project?
				if (form.getAddMe() != null && Boolean.TRUE.equals(form.getAddMe())) {
					Employee employee = getSecurityLogic().getLoggedEmployee();
					final Set<Project> projects;
					if (employee.getProjects() != null) {
						projects = employee.getProjects();
					} else {
						projects = new HashSet<Project>();
						
					}
					//add this project
					projects.add(project);
					//add also parent
					if (project.getParent() != null) {
						projects.add(project.getParent());
					}
					employee.setProjects(projects);
					getEmployeeLogic().store(employee);
					getSecurityLogic().reloadLoggedEmployee();
				}

			} catch(Exception e){
				requestContext.addError(new Message("projectManagement.create.error"));
			}
			
			//some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("projectManagement.create.success"));
			}
		}

		return createModelAndView(view);
	}	
}
