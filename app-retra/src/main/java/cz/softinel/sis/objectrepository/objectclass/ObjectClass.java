package cz.softinel.sis.objectrepository.objectclass;

import cz.softinel.retra.project.Project;

public class ObjectClass {

	private Long pk;

	private String id;
	
	// Business fields ...

	// Getter and Setters ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}
	
	
	// Object implementation ...
	
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
		if (!(obj instanceof Project)) {
			return false;
		}
		ObjectClass that = (ObjectClass) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(ObjectClass: pk=" + pk + ", id=" + id +")";
	}

}
