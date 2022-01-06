package cz.softinel.retra.worklog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.component.Component;
import cz.softinel.retra.core.utils.helper.DateHelper;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;
import cz.softinel.retra.project.Project;

/**
 * This class represents one worklog item.
 * 
 * @version $Revision: 1.9 $ $Date: 2007-02-23 07:29:38 $
 * @author Petr Sígl
 */
public class Worklog {

	// attributes
	private Long pk;

	private Date workFrom;
	private Date workTo;
	private String description;

	// counted field
	private String descriptionGui;

	private Employee employee;
	private Activity activity;
	private Project project;
	private Component component;
	private Invoice invoice;

	private Set<JiraWorklog> issueTrackingWorklogs;

	// constants
	public static Long DUMMY_WORKLOG_PK = (long) -1;

	// Business fileds ...

	public BigDecimal getHours() {
		// TODO: Use helper method???
		long deltaMiliseconds = workTo.getTime() - workFrom.getTime();
		return new BigDecimal(deltaMiliseconds).divide(new BigDecimal(3600000), 10, RoundingMode.CEILING);
	}

	public long getSeconds() {
		// TODO: Use helper method???
		long deltaMiliseconds = workTo.getTime() - workFrom.getTime();
		return deltaMiliseconds / 1000;
	}

	public Date getDate() {
		return DateHelper.getStartOfDay(workFrom);
	}

	public String getWeek() {
		return DateHelper.getWeekCode(workFrom);
	}

	// Entity fields ...

	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
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

	public String getDescriptionGui() {
		return descriptionGui;
	}

	public void setDescriptionGui(String descriptionGui) {
		this.descriptionGui = descriptionGui;
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
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
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
	 * @return the issueTrackingWorklogs
	 */
	public Set<JiraWorklog> getIssueTrackingWorklogs() {
		return issueTrackingWorklogs;
	}

	/**
	 * @param issueTrackingWorklogs the issueTrackingWorklogs to set
	 */
	public void setIssueTrackingWorklogs(Set<JiraWorklog> issueTrackingWorklogs) {
		this.issueTrackingWorklogs = issueTrackingWorklogs;
	}

	/**
	 * 
	 * @param issueTrackingWorklog
	 */
	public void addIssueTrackingWorklog(JiraWorklog issueTrackingWorklog) {
		if (issueTrackingWorklogs == null) {
			issueTrackingWorklogs = new HashSet<JiraWorklog>();
		}
		issueTrackingWorklogs.add(issueTrackingWorklog);
		issueTrackingWorklog.setWorklog(this);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasAnyIssueTrackingWorklog() {
		return issueTrackingWorklogs != null && !issueTrackingWorklogs.isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	public JiraWorklog getCurrentIssueTrackingWorklog() {
		return hasAnyIssueTrackingWorklog()
				? (JiraWorklog) issueTrackingWorklogs.toArray()[issueTrackingWorklogs.size() - 1]
				: null;
	}

	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * Returns true if worklog is on some invoice.
	 * 
	 * @return
	 */
	public boolean isOnInvoice() {
		if (invoice != null && invoice.getPk() != null) {
			return true;
		}

		return false;
	}

	/**
	 * Returns true if worklog is on active invoice.
	 * 
	 * @return
	 */
	public boolean isEditableAccordingToInvoice() {
		if (isOnInvoice()) {
			if (invoice.getState() == Invoice.STATE_ACTIVE) {
				return true;
			}

			return false;
		}

		return true;
	}

	public String toString() {
		return "(Worklog: pk=" + pk + ", workFrom=" + workFrom + ", workTo=" + workTo + ", description=" + description
				+  ", employee="
				+ (employee != null ? employee.toString() : "")
				+  ", activity="
				+ (activity != null ? activity.getCodeAndName() : "")
				+  ", project="
				+ (project != null ? project.getCodeAndName() : "")
				+  ", component="
				+ (component != null ? component.getCodeAndName() : "")
				+  ", invoice="
				+ (invoice != null ? invoice.getCodeAndName() : "")
				+ ")";
	}


}
