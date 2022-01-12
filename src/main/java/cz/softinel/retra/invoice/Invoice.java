package cz.softinel.retra.invoice;

import java.util.Date;

import cz.softinel.retra.employee.Employee;
import cz.softinel.uaf.state.StateEntity;

/**
 * This class represents invoice.
 * 
 * @author Petr Sígl
 */
public class Invoice implements StateEntity {

	// attributes
	private Long pk;

	private Date orderDate;
	private Date startDate;
	private Date finishDate;

	private String code;
	private String name;

	private Employee employee;

	private int state = Invoice.STATE_ACTIVE;

	// constants
	public static Long DUMMY_INVOICE_PK = (long) -1;

	// Business fields ...

	public String getCodeAndName() {
		return InvoiceHelper.getCodeAndName(this);
	}

	// Getter and Setters ...

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

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
		if (!(obj instanceof Invoice)) {
			return false;
		}
		Invoice that = (Invoice) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Invoice: pk=" + pk + ", code=" + code + ", name=" + name + ")";
	}

}
