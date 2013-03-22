package cz.softinel.retra.project;

import java.math.BigDecimal;
import java.util.Set;

import cz.softinel.retra.category.Category;
import cz.softinel.retra.component.Component;
import cz.softinel.retra.employee.Employee;
import cz.softinel.uaf.state.StateEntity;

/**
 * This class represents project.
 * 
 * @version $Revision: 1.6 $ $Date: 2007-08-19 19:51:55 $
 * @author Radek Pinc, Petr Sígl
 */
public class Project implements StateEntity {

	// attributes
	private Long pk;

	private Project parent;
	private String code;
	private String name;
	private Boolean workEnabled;
	private BigDecimal estimation;

	private Category category;
	
	private Set<Employee> employees;
	private Set<Component> components;

	private int state = Project.STATE_ACTIVE;
	
	//constants
	public static Long DUMMY_PROJECT_PK = (long)-1;

	// Business fields ...

	public String getCodeAndName() {
		return ProjectHelper.getCodeAndName(this);
	}

	// Getter and Setters ...

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the parent.
	 */
	public Project getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Project parent) {
		this.parent = parent;
	}

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
	public BigDecimal getEstimation() {
		return estimation;
	}

	/**
	 * @param estimation the estimation to set
	 */
	public void setEstimation(BigDecimal estimation) {
		this.estimation = estimation;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return the Set of employees belong to this project
	 */
	public Set<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees set the employees of the project
	 */
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}	
	
	/**
	 * @return the Set of components belong to this project
	 */
	public Set<Component> getComponents() {
		return components;
	}

	/**
	 * @param components set the components of the project
	 */
	public void setComponents(Set<Component> components) {
		this.components = components;
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
		Project that = (Project) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Project: pk=" + pk + ", code=" + code + ", name="+ name+")";
	}
	
}
