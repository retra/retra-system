package cz.softinel.sis.objectrepository.attribute;

import cz.softinel.retra.project.Project;
import cz.softinel.sis.objectrepository.objectinstance.ObjectInstance;

public class Attribute {

	private Long pk;

	private String name;
	private String title;
	private String type;
	private String typeClass;
	
	private ObjectInstance objectInstance;

	// Business fields ...

	// Getter and Setters ...

	public ObjectInstance getObjectInstance() {
		return objectInstance;
	}

	public void setObjectInstance(ObjectInstance objectInstance) {
		this.objectInstance = objectInstance;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
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
		Attribute that = (Attribute) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Attribute: pk=" + pk + ", name=" + name +", title=" + title +", type=" + type +", typeClass=" + typeClass +")";
	}

}
