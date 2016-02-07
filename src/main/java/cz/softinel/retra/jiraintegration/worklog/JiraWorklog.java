package cz.softinel.retra.jiraintegration.worklog;

import java.util.Date;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.worklog.Worklog;



/**
 * Worklog stored locally for the Jira system.
 *
 */
@Deprecated
public class JiraWorklog {
	
	public static final int EXPORTED = 0;
	public static final int CREATED = 1;
	public static final int UPDATED = 2;
	/**
	 * This is used when the Jira issue field is modified for an existing worklog. Then a delete and update are needed.
	 */
	public static final int ISSUE_UPDATED = 3;
	/**
	 * This is used when the jira issue field is updated and the new Jira issue does not exist.
	 */
	public static final int EXPORTED_FOR_NONEXISTENT = 4;
	
	// attributes
	
	private Long pk;
	private Employee employee;
	private Worklog worklog;
	private String jiraIssue;
	private Date created;
	private long timeSpentInSeconds;
	private int state;
	/**
	 * This variable stores the ID got from the remote Jira system.
	 * @author Erik Szalai
	 */
	private Long remoteId;
	
	// getters and setters
	
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
	/**
	 * @return the worklog
	 */
	public Worklog getWorklog() {
		return worklog;
	}
	/**
	 * @param worklog the worklog to set
	 */
	public void setWorklog(Worklog worklog) {
		this.worklog = worklog;
	}
	/**
	 * @return the jiraIssue
	 */
	public String getJiraIssue() {
		return jiraIssue;
	}
	/**
	 * @param jiraIssue the jiraIssue to set
	 */
	public void setJiraIssue(String jiraIssue) {
		this.jiraIssue = jiraIssue;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return the timeSpentInSeconds
	 */
	public long getTimeSpentInSeconds() {
		return timeSpentInSeconds;
	}
	/**
	 * @param timeSpentInSeconds the timeSpentInSeconds to set
	 */
	public void setTimeSpentInSeconds(long timeSpentInSeconds) {
		this.timeSpentInSeconds = timeSpentInSeconds;
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
	 * Get the ID of the worklog given by the remote Jira and saved when the worklog was added.
	 * @return
	 */
	public Long getRemoteId() {
		return remoteId;
	}
	/**
	 * Set the worklog ID, which shall be got from the remote Jira, when adding or updating the worklog.
	 * @param remoteId
	 */
	public void setRemoteId(Long remoteId) {
		this.remoteId = remoteId;
	}
	
}
