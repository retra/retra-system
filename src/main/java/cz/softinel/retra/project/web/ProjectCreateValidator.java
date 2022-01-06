package cz.softinel.retra.project.web;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cz.softinel.retra.project.Project;

public class ProjectCreateValidator extends ProjectValidator implements Validator {

	protected void validateCodeUniqness(Errors errors, String code) {
		// TODO : the query can be optimized with obtaining only the count
		List<Project> projects = getProjectLogic().findProjectsWithCode(code);
		if (projects.size() > 0) {
			errors.reject("projectManagement.create.project.exists");
		}
	}

}
