package cz.softinel.retra.project.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.convertor.BigDecimalConvertor;
import cz.softinel.retra.project.blo.ProjectLogic;

public abstract class ProjectValidator implements Validator {

	private ProjectLogic projectLogic;

	public boolean supports(Class clazz) {
		return clazz.equals(ProjectForm.class);
	}

	public void validate(Object command, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "projectManagement.error.require.name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "projectManagement.error.require.code");
		String code = (String) errors.getFieldValue("code");
		if (!(code == null || code.length() == 0)) {
			validateCodeUniqness(errors, code);
		}

		String estimation = (String) errors.getFieldValue("estimation");
		if (!(estimation == null || estimation.length() == 0)) {
			if (BigDecimalConvertor.getBigDecimalFromString(estimation) == null) {
				errors.rejectValue("estimation", "projectForm.estimation.bad.format");
			}
		}
	}

	protected abstract void validateCodeUniqness(Errors errors, String code);

	public ProjectLogic getProjectLogic() {
		return projectLogic;
	}

	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

}
