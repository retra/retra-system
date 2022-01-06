package cz.softinel.retra.employee.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.validator.CommonValidator;

public class EmployeeChangeContactInfoValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(EmployeeForm.class);
	}

	public void validate(Object command, Errors errors) {

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.firstName",
				"employeeManagement.changePassword.error.require.firstName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.lastName",
				"employeeManagement.changePassword.error.require.lastName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.email",
				"employeeManagement.changePassword.error.require.email");
		CommonValidator.validateEmail("user.contactInfo.email", errors,
				"employeeManagement.changePassword.error.badEmail", null);
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.web", "xxxxx");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.phone1", "xxxxx");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.phone2", "xxxxx");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "user.contactInfo.fax", "xxxxx");
	}

}
