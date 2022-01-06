package cz.softinel.retra.schedule.web;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.validator.CommonValidator;

public class ScheduleValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(ScheduleForm.class);
	}

	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "scheduleForm.date.required");
		CommonValidator.validateDate("date", errors, "scheduleForm.date.bad.format", null);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "scheduleForm.type.required");

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workFrom", "scheduleForm.workFrom.required");
		CommonValidator.validateHour("workFrom", errors, "scheduleForm.workFrom.bad.format", null);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workTo", "scheduleForm.workTo.required");
		CommonValidator.validateHour("workTo", errors, "scheduleForm.workTo.bad.format", null);

		validateToGreaterThanFrom(errors);
	}

	private void validateToGreaterThanFrom(Errors errors) {
		// FUJ: this is not realy nice, but it works
		String date = DateConvertor.convertToDateStringFromDate(new Date());
		String first = (String) errors.getFieldValue("workTo");
		String second = (String) errors.getFieldValue("workFrom");

		if (CommonValidator.isValidHour(first) && CommonValidator.isValidHour(second)) {
			Date to = null;
			Date from = null;
			try {
				to = DateConvertor.convertToDateFromDateStringHourString(date, first);
				from = DateConvertor.convertToDateFromDateStringHourString(date, second);
			} catch (ConvertException e) {
				// couldn't compare return
				return;
			}

			if (from.getTime() >= to.getTime()) {
				errors.reject("scheduleForm.workTo.greater.workForm");
			}
		}
	}
}
