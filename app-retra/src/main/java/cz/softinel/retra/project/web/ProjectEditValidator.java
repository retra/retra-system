package cz.softinel.retra.project.web;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.project.Project;


public class ProjectEditValidator extends ProjectValidator implements Validator {
	
	protected void validateCodeUniqness(Errors errors, String code){		
		String pk = (String) errors.getFieldValue("pk");
		Long pkLong = LongConvertor.getLongFromString(pk);

		List<Project> projects = getProjectLogic().findProjectsWithCode(code);
		if (projects.size() > 1) {
			errors.reject("projectManagement.create.project.exists");
		} else if (projects.size() == 1) {
			if (!projects.get(0).getPk().equals(pkLong)) {
				errors.reject("projectManagement.create.project.exists");
			}
		}
	}

}
