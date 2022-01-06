package cz.softinel.retra.component;

import cz.softinel.retra.project.Project;
import cz.softinel.uaf.state.StateEntity;

/**
 * This class represents component.
 * 
 * @author Zoltan Vadasz
 */
public class Component implements StateEntity {

	// attributes

	private Long pk;
	private Project project;
	private String code;
	private String name;

	private int state = Component.STATE_ACTIVE;

	// Business fields ...

	public String getCodeAndName() {
		return ComponentHelper.getCodeAndName(this);
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state set the state
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the pk
	 */
	public Long getPk() {
		return pk;
	}

	/**
	 * @param pk sets the pk
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}

	/**
	 * @return Project of the component
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param parent set the project Project
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the code of the component
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code set the code of the component
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name of the component
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name sets the name of the component
	 */
	public void setName(String name) {
		this.name = name;
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
		if (!(obj instanceof Component)) {
			return false;
		}
		Component that = (Component) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Component: pk=" + pk + ", code=" + code + ", name=" + name + ")";
	}

}
