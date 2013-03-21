package cz.softinel.retra.employee.web;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.EmployeeHelper;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.sis.contactinfo.blo.ContactInfoLogic;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class EmployeeChangeContactInfoController extends FormController {
	
	private ContactInfoLogic contactInfoLogic;

	// Configuration setter methods ..
	
	public void setContactInfoLogic(ContactInfoLogic contactInfoLogic) {
		this.contactInfoLogic = contactInfoLogic;
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
			Employee employee = getSecurityLogic().getLoggedEmployee();
			// Map form into entities ...
			EmployeeHelper.formToEntity(form, employee);
			// Prepare messages (TODO: It is not so good solutions)
			Messages messages = new Messages(getApplicationContext());
			// Process business logic ...
			contactInfoLogic.store(employee.getUser().getContactInfo());
			// Add messages from bussines into our messages
			requestContext.addAllMessages(messages);
			// Some errors encountered -> do not save and show form view
			if (messages.hasErrors()) {
				model.put(getCommandName(), form);
				return createModelAndView(model, getFormView());
			}
			requestContext.addRedirectIgnoreInfo(new Message("employeeManagement.changeContactInfo.success"));
		}

		return createModelAndView(view);
	}
	

}
