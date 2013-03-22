package cz.softinel.retra.schedule.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.validator.CommonValidator;
import cz.softinel.retra.core.utils.validator.ValidationException;

public class ScheduleCopyValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(ScheduleForm.class);
	}

	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "scheduleForm.date.required");
		CommonValidator.validateDate("date", errors, "scheduleForm.date.bad.format", null);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "copyDestinationFrom", "scheduleForm.copyDestinationFrom.required");
		CommonValidator.validateDate("copyDestinationFrom", errors, "scheduleForm.copyDestinationFrom.bad.format", null);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "copyDestinationTo", "scheduleForm.copyDestinationTo.required");
		CommonValidator.validateDate("copyDestinationTo", errors, "scheduleForm.copyDestinationTo.bad.format", null);

		validateDestinationFormGreaterDate(errors);
		validateDestinationToGreaterDate(errors);
		validateDestinationToGreaterOrEqualDestinationForm(errors);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "copyType", "scheduleForm.copyType.required");
	}

	private void validateDestinationFormGreaterDate(Errors errors){
		try {
			if (!CommonValidator.isFirstGreaterThanSecond("copyDestinationFrom", "date", errors)){
				errors.reject("scheduleForm.copyDestinationFrom.greater.date");
			}
		}
		catch (ValidationException e) {
			//validation exception -> do nothing
		}
	}

	private void validateDestinationToGreaterDate(Errors errors){
		try {
			if (!CommonValidator.isFirstGreaterThanSecond("copyDestinationTo", "date", errors)){
				errors.reject("scheduleForm.copyDestinationTo.greater.date");
			}
		}
		catch (ValidationException e) {
			//validation exception -> do nothing
		}
	}

	private void validateDestinationToGreaterOrEqualDestinationForm(Errors errors){
		try {
			if (!CommonValidator.isFirstGreaterOrEqualThanSecond("copyDestinationTo", "copyDestinationFrom", errors)){
				errors.reject("scheduleForm.copyDestinationTo.greaterOrEquals.copyDestinationFrom");
			}
		}
		catch (ValidationException e) {
			//validation exception -> do nothing
		}
	}

}

