package cz.softinel.retra.employee.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.uaf.util.CommonHelper;

public class EmployeeChangePasswordValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(EmployeeForm.class);
	}

	public void validate(Object command, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.login.passwordOriginal",
				"employeeManagement.changePassword.error.require.original");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.login.password",
				"employeeManagement.changePassword.error.require.new");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.login.passwordConfirmation",
				"employeeManagement.changePassword.error.require.confirmation");

		String password = (String) errors.getFieldValue("user.login.password");
		String passwordConfirmation = (String) errors.getFieldValue("user.login.passwordConfirmation");

		if (CommonHelper.isNotEquals(password, passwordConfirmation)) {
			errors.rejectValue("user.login.passwordConfirmation",
					"employeeManagement.changePassword.error.passwordsNotEquals");
		}

	}

}
