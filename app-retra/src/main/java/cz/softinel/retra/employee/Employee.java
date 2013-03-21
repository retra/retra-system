/*
 * Created on 9.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee;

import java.util.Set;

import cz.softinel.retra.project.Project;
import cz.softinel.sis.user.User;

/**
 * @author Radek Pinc
 *
 */
public class Employee {

	private Long pk;
	
	private User user;
	
	private Set<Project> projects;
	
	// Business fields ...
	
	// Getter and Setters ...
	
	/**
	 * @return Returns the pk.
	 */
	public Long getPk() {
		return pk;
	}
	/**
	 * @param pk The pk to set.
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}
	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return Returns the projects of the user
	 */
	public Set<Project> getProjects() {
		return projects;
	}
	/**
	 * @param projects Projects of the user to set.
	 */
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (this.pk == null) {
			return super.hashCode();
		}
		return this.pk.hashCode();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee that = (Employee) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Employee: pk=" + pk + 
				", name=" + (user != null && user.getContactInfo() != null ? user.getContactInfo().getFirstName() + " " + user.getContactInfo().getLastName() : "") + ")";
	}
	
}
