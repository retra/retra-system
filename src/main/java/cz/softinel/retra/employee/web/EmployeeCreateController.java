package cz.softinel.retra.employee.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.EmployeeHelper;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.LoginHelper;
import cz.softinel.sis.user.User;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class EmployeeCreateController extends AbstractEmployeeFormController {

	// Action methods ...

	public void showForm(Model model, RequestContext requestContext, BindException errors) throws Exception {
		super.showForm(model, requestContext, errors);
		prepareIcompanies(model);
	}
	
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
			employee.getUser().setContactInfo(new ContactInfo());
			// Map form into entities ...
			EmployeeHelper.formToEntity(form, employee);
			// Hash password for create ..
			employee.getUser().getLogin().setPassword(LoginHelper.hashPassword(employee.getUser().getLogin().getPassword()));

			// Process business logic ...
			try{
				getEmployeeLogic().create(employee);
			} catch(Exception e){
				requestContext.addError(new Message("employeeManagement.create.username.exists"));
			}
			
			//some errors encountered -> do not save and show form view
			if (requestContext.getErrors().size() > 0) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("employeeManagement.create.success"));
			}
		}

		return createModelAndView(view);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.CommonFormController#doBeforeCancelAction(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	protected void doBeforeCancelAction(Model model, RequestContext requestContext, Object command, BindException errors) {
//		getCookieHelper().cleanCookies(requestContext);
	}
}
