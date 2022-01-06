package cz.softinel.retra.activity;

import cz.softinel.uaf.state.StateEntity;

/**
 * This class represents employee activity.
 * 
 * @version $Revision: 1.5 $ $Date: 2007-04-03 10:41:00 $
 * @author Petr Sígl
 */
public class Activity implements StateEntity {

	// attributes
	private Long pk;

	private String code;
	private String name;
	private String description;

	private int state;

	// Business fields ...

	public String getCodeAndName() {
		return ActivityHelper.getCodeAndName(this);
	}

	// Getter and Setters ...

	/**
	 * @return the pk
	 */
	public Long getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
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
		if (!(obj instanceof Activity)) {
			return false;
		}
		Activity that = (Activity) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Activity: pk=" + pk + ", code=" + code + ", name=" + name + ")";
	}

}
