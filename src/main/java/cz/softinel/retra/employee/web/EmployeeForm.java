package cz.softinel.retra.employee.web;

import java.util.Set;

import cz.softinel.sis.user.web.UserForm;
import cz.softinel.retra.project.Project;

public class EmployeeForm {

	private String pk;
	
	private Boolean igenerate;
	private String icompany; 

	UserForm user;
	Set<Project> projects;

	// Getters and setters ...

	// TODO: synchronized ???
	public UserForm getUser() {
		if (user == null) {
			user = new UserForm();
		}
		return user;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public Boolean getIgenerate() {
		return igenerate;
	}

	public void setIgenerate(Boolean igenerate) {
		this.igenerate = igenerate;
	}

	public String getIcompany() {
		return icompany;
	}

	public void setIcompany(String icompany) {
		this.icompany = icompany;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
}
