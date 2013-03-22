package cz.softinel.retra.employee.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.validator.CommonValidator;
import cz.softinel.sis.login.LoginHelper;
import cz.softinel.uaf.util.CommonHelper;

public class EmployeeCreateValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(EmployeeForm.class);
	}

	public void validate(Object command, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.firstName", "employeeManagement.changePassword.error.require.firstName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.lastName", "employeeManagement.changePassword.error.require.lastName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.email", "employeeManagement.changePassword.error.require.email");
		CommonValidator.validateEmail("user.contactInfo.email", errors, "employeeManagement.changePassword.error.badEmail", null);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.login.name", "employeeManagement.changePassword.error.require.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.login.password", "employeeManagement.changePassword.error.require.new");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.login.passwordConfirmation", "employeeManagement.changePassword.error.require.confirmation");
		
		String password = (String) errors.getFieldValue("user.login.password");
		String passwordConfirmation = (String) errors.getFieldValue("user.login.passwordConfirmation");
		
		if (CommonHelper.isNotEquals(password, passwordConfirmation)) {
			errors.rejectValue("user.login.passwordConfirmation", "employeeManagement.changePassword.error.passwordsNotEquals");
		}
		if(!LoginHelper.isPasswordSecure(password)){
			errors.rejectValue("user.login.password", "employeeManagement.create.password.not.secure");
		}
	}
	
}
