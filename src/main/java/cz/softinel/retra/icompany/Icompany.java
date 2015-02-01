package cz.softinel.retra.icompany;

import cz.softinel.uaf.state.StateEntity;


/**
 * This class represents invoice company.
 * 
 * @author Petr Sígl
 */
public class Icompany implements StateEntity {

	// attributes
	private Long pk;

	private String code;
	private String name;

	private int state = Icompany.STATE_ACTIVE;

	//constants
	public static Long DUMMY_CATEGORY_PK = (long)-1;
	
	// Business fields ...

	public String getCodeAndName() {
		return IcompanyHelper.getCodeAndName(this);
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
		if (!(obj instanceof Icompany)) {
			return false;
		}
		Icompany that = (Icompany) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Icompany: pk=" + pk + ", code=" + code + ", name="+ name+")";
	}

}
