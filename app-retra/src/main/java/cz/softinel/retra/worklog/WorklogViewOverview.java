package cz.softinel.retra.worklog;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.component.Component;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.project.Project;

@SuppressWarnings("serial")
public class WorklogViewOverview implements Serializable {

	private Date date;
	private BigDecimal hours;

	private Employee employee;
	private Activity activity;
	private Project project;
	private Component component;
	
	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the hours
	 */
	public BigDecimal getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	
	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
	}
	
	
}
