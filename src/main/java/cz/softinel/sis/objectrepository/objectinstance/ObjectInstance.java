package cz.softinel.sis.objectrepository.objectinstance;

import cz.softinel.retra.project.Project;
import cz.softinel.sis.objectrepository.objectclass.ObjectClass;

public class ObjectInstance {

	private Long pk;

	private String id;
	
	private ObjectClass objectClass;

	// Business fields ...

	// Getter and Setters ...

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ObjectClass getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(ObjectClass objectClass) {
		this.objectClass = objectClass;
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
		ObjectInstance that = (ObjectInstance) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(ObjectInstance: pk=" + pk + ", id=" + id +")";
	}

}
