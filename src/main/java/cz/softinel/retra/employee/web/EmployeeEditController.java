package cz.softinel.retra.employee.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.EmployeeHelper;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.sis.contactinfo.blo.ContactInfoLogic;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * Controller class for editing an Employee
 * 
 * @author Zoltan Vadasz
 */
public class EmployeeEditController extends AbstractEmployeeFormController {

	private ContactInfoLogic contactInfoLogic;
	private ProjectLogic projectLogic;

	// Configuration setter methods ..

	public void setContactInfoLogic(ContactInfoLogic contactInfoLogic) {
		this.contactInfoLogic = contactInfoLogic;
	}

	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

	// Action methods ...

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		// Read input parameters ...
		String pkString = requestContext.getParameter("pk");
		// Load data ...
		Employee employee = new Employee();
		employee.setPk(Long.valueOf(pkString));
		getEmployeeLogic().loadAndLoadLazy(employee);
		EmployeeForm form = (EmployeeForm) command;
		EmployeeHelper.entityToForm(employee, form);
	}

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
		prepareProjects(model);
		prepareIcompanies(model);
	}

	protected void prepareProjects(Model model) {
		List<Project> projects = null;
		projects = projectLogic.findAllNotDeletedProjects();
		model.put("projects", projects);
	}

	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors)
			throws Exception {

		int action = getAction();
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			// Prepare form ...
			EmployeeForm form = (EmployeeForm) command;
			// Prepare entities ...
			Employee employee = new Employee();
			employee.setPk(Long.valueOf(Long.valueOf(form.getPk())));
			getEmployeeLogic().loadAndLoadLazy(employee);
			// Map form into entities ...
			EmployeeHelper.formToEntity(form, employee);
			// Process business logic ...
			contactInfoLogic.store(employee.getUser().getContactInfo());
			// Add parent projects to the employee's projects, too
			final Set<Project> projects = new HashSet<Project>();
			if (employee.getProjects() != null) {
				for (final Project p : employee.getProjects()) {
					projects.add(p);
					if (p.getParent() != null) {
						projects.add(p.getParent());
					}
				}
			}
			employee.setProjects(projects);
			getEmployeeLogic().store(employee);
			// Some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			} else {
				getSecurityLogic().reloadLoggedEmployee();
				requestContext.addRedirectIgnoreInfo(new Message("employeeManagement.edit.success"));
			}
		}
		return createModelAndView(view);
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Set.class, "projects", new CustomCollectionEditor(Set.class) {
			protected Object convertElement(Object element) {
				if (element != null) {
					Long id = new Long((String) element);
					Project project = projectLogic.get(id);
					return project;
				}
				return null;
			}
		});
	}
}
