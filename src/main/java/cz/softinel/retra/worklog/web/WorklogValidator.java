package cz.softinel.retra.worklog.web;

import java.util.Date;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.core.utils.validator.CommonValidator;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;

public class WorklogValidator implements Validator {

	private ProjectLogic projectLogic;
	
	public boolean supports(Class clazz) {
		return clazz.equals(WorklogForm.class);
	}

	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "worklogForm.date.required");
		CommonValidator.validateDate("date", errors, "worklogForm.date.bad.format", null);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "project", "worklogForm.project.required");
		if (errors.getFieldValue("project") != null) {
			Project project = getProjectLogic().get(LongConvertor.getLongFromString((String)errors.getFieldValue("project")));
			if (project.getWorkEnabled() == null || !project.getWorkEnabled()) {
				errors.reject("worklogForm.project.worklogDisabled");
			}
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "activity", "worklogForm.activity.required");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workFrom", "worklogForm.workFrom.required");
		CommonValidator.validateHour("workFrom", errors, "worklogForm.workFrom.bad.format", null);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "workTo", "worklogForm.workTo.required");
		CommonValidator.validateHour("workTo", errors, "worklogForm.workTo.bad.format", null);
		
		validateToGreaterThanFrom(errors);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "worklogForm.description.required");
	}
	
	private void validateToGreaterThanFrom(Errors errors){
		//FUJ: this is not realy nice, but it works
		String date = DateConvertor.convertToDateStringFromDate(new Date());
		String first = (String)errors.getFieldValue("workTo");
		String second = (String)errors.getFieldValue("workFrom");
		
		if (CommonValidator.isValidHour(first) && CommonValidator.isValidHour(second)){
			Date to = null;
			Date from = null;
			try {
				to = DateConvertor.convertToDateFromDateStringHourString(date, first);
				from = DateConvertor.convertToDateFromDateStringHourString(date, second);
			}
			catch (ConvertException e) {
				//couldn't compare return
				return;
			}
			
			if (from.getTime() >= to.getTime()) {
				errors.reject("worklogForm.workTo.greater.workForm");
			}
		}
	}

	public ProjectLogic getProjectLogic() {
		return projectLogic;
	}

	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

}
