package cz.softinel.retra.project.web;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.component.Component;
import cz.softinel.retra.component.blo.ComponentLogic;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.invoice.InvoiceHelper;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.retra.project.dao.ProjectFilter;
import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.CommonDispatchController;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class ProjectController extends CommonDispatchController {
	
	private ProjectLogic projectLogic;
	private ComponentLogic componentLogic;
	private MiraSecurityLogic securityLogic;
	private EmployeeLogic employeeLogic;
	
	// Configuration setter methods ..
	
	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}	
	
	public void setComponentLogic(ComponentLogic componentLogic) {
		this.componentLogic = componentLogic;
	}	
	
	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		this.securityLogic = securityLogic;
	}
	
	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	// Action methods ...

	@Override
	public Filter getFilter(Model model) {
		Filter filter = (Filter) model.get(ProjectFilter.class.toString());
		if (filter == null) {
			filter = new ProjectFilter();
			model.set(ProjectFilter.class.toString(), filter);
		}
		return filter;
	}
	
	public ModelAndView projectManagement(Model model, RequestContext requestContext)
	{
		Filter filter = getFilter(model);
		Integer state = FilterHelper.getFieldAsInteger(ProjectFilter.PROJECT_FILTER_STATE, filter);
		
		// state not set, so show active
		if (state == null) {
			state = Project.STATE_ACTIVE;
			FilterHelper.setField(ProjectFilter.PROJECT_FILTER_STATE, state, filter);
		}
		
		prepareEmployees(model);
		prepareProjectStates(model);

		List<Project> list = projectLogic.findByFilter(filter);
		model.set("projects", list);
		
		return createModelAndView(model, getSuccessView());
	}
	
	/**
	 * View given project. 
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView projectView(Model model, RequestContext requestContext)	{
		String strPk = requestContext.getParameter("pk");
		Long pk = LongConvertor.getLongFromString(strPk);
		if (pk != null) {
			Project project = new Project();
			project.setPk(pk);
			projectLogic.loadAndLoadLazy(project);
			model.put("project", project);
		}
		return createModelAndView(model, getSuccessView());
	}
	
	/**
	 * Delete given project
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView projectDelete(Model model, RequestContext requestContext) {
		String projectPkStr = requestContext.getParameter("pk");
		Long projectPk = LongConvertor.getLongFromString(projectPkStr);
		String view = getSuccessView();
		if (projectPk != null) {
			Project project = projectLogic.get(projectPk);
			project.setEmployees(null);
			project.setState(Project.STATE_DELETED);
			projectLogic.store(project);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView();
			} else {
				securityLogic.reloadLoggedEmployee();
				requestContext.addRedirectIgnoreInfo(new Message("Project deleted."));
			}
			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView();
		}
		return createModelAndView(model, view);
	}
	
	/**
	 * Delete given component
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView componentDelete(Model model, RequestContext requestContext) {
		String componentPkStr = requestContext.getParameter("pk");
		Long componentPk = LongConvertor.getLongFromString(componentPkStr);
		String view = getSuccessView();
		String projectPk = null;
		if (componentPk != null) {
			Component component = componentLogic.get(componentPk);
			projectPk = component.getProject().getPk().toString();
			component.setState(Component.STATE_DELETED);
			component.setProject(null);
			componentLogic.store(component);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView();
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("Component deleted."));
			}
			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView();
		}
		return new ModelAndView(view,"pk",projectPk);
	}
	
	/**
	 * Prepare invoice states.
	 * 
	 * @param model
	 */
	private void prepareProjectStates(Model model) {
		model.set("states", InvoiceHelper.INVOICE_STATES);
	}

	/**
	 * Prepare all employess list.
	 * 
	 * @param model
	 */
	private void prepareEmployees(Model model) {
		List<Employee> employees = employeeLogic.getAllEmployeesNotFull();
		model.set("employees", employees);
	}

}
