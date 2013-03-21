package cz.softinel.retra.schedule;

import java.util.Date;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.type.Type;
import cz.softinel.uaf.state.StateEntity;

/**
 * this class represents one schedule item
 * 
 * @version $Revision: 1.4 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl, Radek Pinc
 */
public class Schedule implements StateEntity {

	private Long pk;
	private Date workFrom;
	private Date workTo;
	private int state;
	private String comment;
	private int level;
	private Float hours;
	private Date createdOn;
	private Date changedOn;

	private Type type;
	private Employee employee;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the hours
	 */
	public Float getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(Float hours) {
		this.hours = hours;
	}

	/**
	 * @return the changedOn
	 */
	public Date getChangedOn() {
		return changedOn;
	}

	/**
	 * @param changedOn the changedOn to set
	 */
	public void setChangedOn(Date changedOn) {
		this.changedOn = changedOn;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

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
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @return the workFrom
	 */
	public Date getWorkFrom() {
		return workFrom;
	}

	/**
	 * @param workFrom the workFrom to set
	 */
	public void setWorkFrom(Date workFrom) {
		this.workFrom = workFrom;
	}

	/**
	 * @return the workTo
	 */
	public Date getWorkTo() {
		return workTo;
	}

	/**
	 * @param workTo the workTo to set
	 */
	public void setWorkTo(Date workTo) {
		this.workTo = workTo;
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

	//bussiness and gui methods
	
	/**
	 * This method returns cssClass (for gui) 
	 * @return the css class
	 */
	//TODO: maybe better move somewhere else, but question is where
	public String getCssClass(){
		return ScheduleHelper.getCssClass(this);
	}

}
