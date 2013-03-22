package cz.softinel.retra.project.web;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.component.Component;
import cz.softinel.retra.component.blo.ComponentLogic;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.ProjectHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ProjectEditController extends AbstractProjectFormController {

	private ComponentLogic	componentLogic;
	private Set<Component> 	components = null;
	
	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		ProjectForm projectForm = (ProjectForm) command;
		
		String projectPkStr = requestContext.getParameter("pk");
		Long projectPk = LongConvertor.getLongFromString(projectPkStr);
		/*
		if (projectPk != null){
			Project project = getProjectLogic().get(projectPk);
			ProjectHelper.entityToForm(project, projectForm);
		}
		*/
		Project project = new Project();
		project.setPk(projectPk);
		getProjectLogic().loadAndLoadLazy(project);
		components = project.getComponents();
		ProjectHelper.entityToForm(project, projectForm);
	}
	
	// Configuration setter methods ..
	
	public void setComponentLogic(ComponentLogic componentLogic) {
		this.componentLogic = componentLogic;
	}
	
	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
		prepareProjects(model);
		prepareCategories(model);
		prepareAllEmployees(model);
	}	
	
	protected void prepareAllEmployees(Model model) {
		List<Employee> employees = null;
		employees = getEmployeeLogic().getAllEmployeesNotFull();	
		//put employees into the model
		model.put("employees", employees);
	}	
	
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors) throws Exception {
		
		int action = getAction();
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			// Prepare form ...
			ProjectForm form = (ProjectForm) command;
			// Prepare entities ...
			Project project = new Project();
			project.setPk(Long.valueOf(form.getPk()));
			getProjectLogic().loadAndLoadLazy(project);
			// Map form into entities ...
			ProjectHelper.formToEntity(form, project);
			// Add the same employees to the parent project, too
			if ( (project.getEmployees() != null) && (project.getParent() != null) ){
				Project parentProject =new Project();
				parentProject.setPk(project.getParent().getPk());
				getProjectLogic().loadAndLoadLazy(parentProject);
				if (parentProject.getEmployees() != null) {
					parentProject.getEmployees().addAll(project.getEmployees());
				} else {
					parentProject.setEmployees(project.getEmployees());
				}
				getProjectLogic().store(parentProject);
			}
			project.setComponents(components);
			getProjectLogic().store(project);
			// Some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			} else {
				getSecurityLogic().reloadLoggedEmployee();
				requestContext.addRedirectIgnoreInfo(new Message("projectManagement.edit.success"));
			}
		}
		return createModelAndView(view);
	}	
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		
		 binder.registerCustomEditor(Set.class, "employees", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   Long id = new Long((String)element);
	                   Employee employee = getEmployeeLogic().get(id);
	                   return employee;
	               }
	               return null;
	           }
	       });
		binder.registerCustomEditor(Set.class, "components", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   Long id = new Long((String)element);
	                   Component component = componentLogic.get(id);
	                   return component;
	               }
	               return null;
	           }
	       });
	   }
}
