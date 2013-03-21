package cz.softinel.retra.employee.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.EmployeeHelper;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.blo.LoginLogic;
import cz.softinel.sis.login.web.LoginForm;
import cz.softinel.sis.user.User;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class EmployeeChangePasswordController extends FormController {
	
	private LoginLogic loginLogic;

	// Configuration setter methods ..
	
	public void setLoginLogic(LoginLogic loginLogic) {
		this.loginLogic = loginLogic;
	}

	// Action methods ...

	public void onBindOnNewForm(Model model, RequestContext requestContext, Object command) throws Exception {
		EmployeeForm form = (EmployeeForm) command;
		Employee employee = getSecurityLogic().getLoggedEmployee();
		EmployeeHelper.entityToForm(employee, form);
	}

	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(Model model, RequestContext requestContext, Object command, BindException errors) throws Exception {
		
		int action = getAction();
		String view = getSuccessView();
		if (action == ACTION_SAVE) {
			// Prepare form ...
			EmployeeForm form = (EmployeeForm) command;
			// Prepare entities ...
			Employee employee = new Employee();
			employee.setUser(new User());
			employee.getUser().setLogin(new Login());
			// Map form into entities ...
			EmployeeHelper.formToEntity(form, employee);
			
			
			// Process business logic ...
			User loggedUser = getSecurityLogic().getLoggedUser();
			LoginForm loginForm = form.getUser().getLogin();
			loginLogic.changePassword(loggedUser.getLogin(), loginForm.getPasswordOriginal(), loginForm.getPassword());

			// Some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			}
			
			requestContext.addRedirectIgnoreInfo(new Message("employeeManagement.changePassword.success"));
		}

		return createModelAndView(view);
	}
	

}
