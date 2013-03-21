package cz.softinel.retra.project.web;

import java.util.Set;

import cz.softinel.retra.component.Component;
import cz.softinel.retra.employee.Employee;

public class ProjectForm {

	private String pk;

	private String name;
	private String code;
	// TODO: list of Project or ProjectForm ?
	// private List<Project> projects;
	private String parentPk;
	private String category;
	
	private Boolean addMe;
	private Boolean workEnabled;
	private String estimation;
	
	private Set<Employee> employees;
	private Set<Component> components;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getParentPk() {
		if (parentPk == null) {
			parentPk = "";
		}
		return parentPk;
	}

	public void setParentPk(String parent) {
		this.parentPk = parent;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Set<Component> getComponents() {
		return components;
	}

	public void setComponents(Set<Component> components) {
		this.components = components;
	}

	public Boolean getAddMe() {
		return addMe;
	}

	public void setAddMe(Boolean addMe) {
		this.addMe = addMe;
	}

	/**
	 * @return the workEnabled
	 */
	public Boolean getWorkEnabled() {
		return workEnabled;
	}

	/**
	 * @param workEnabled the workEnabled to set
	 */
	public void setWorkEnabled(Boolean workEnabled) {
		this.workEnabled = workEnabled;
	}

	/**
	 * @return the estimation
	 */
	public String getEstimation() {
		return estimation;
	}

	/**
	 * @param estimation the estimation to set
	 */
	public void setEstimation(String estimation) {
		this.estimation = estimation;
	}
	
}
