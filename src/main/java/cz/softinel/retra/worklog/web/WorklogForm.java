package cz.softinel.retra.worklog.web;

/**
 * This class is used for binding workFrom jsp workTo entity.
 *
 * @version $Revision: 1.3 $ $Date: 2007-01-24 09:12:02 $
 * @author Petr Sígl
 */
public class WorklogForm {

	private String pk;
	private String date;
	private String workFrom;
	private String workTo;
	private String description;
	private String project;
	private String component;
	private String activity;
	private String issueTrackingReference;
	private String invoice;

	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the pk
	 */
	public String getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the component
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * @return the workFrom
	 */
	public String getWorkFrom() {
		return workFrom;
	}

	/**
	 * @param workFrom the workFrom to set
	 */
	public void setWorkFrom(String workFrom) {
		this.workFrom = workFrom;
	}

	/**
	 * @return the workTo
	 */
	public String getWorkTo() {
		return workTo;
	}

	/**
	 * @param workTo the workTo to set
	 */
	public void setWorkTo(String workTo) {
		this.workTo = workTo;
	}

	/**
	 * @return the issueTrackingReference
	 */
	public String getIssueTrackingReference() {
		return issueTrackingReference;
	}

	/**
	 * @param issueTrackingReference the issueTrackingReference to set
	 */
	public void setIssueTrackingReference(String issueTrackingReference) {
		this.issueTrackingReference = issueTrackingReference;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "(WorklogForm: pk=" + pk + ", date=" + date + ", workFrom=" + workFrom + ", workTo=" + workTo
				+ ", description=" + description + ", project=" + project + ", component=" + component + ", activity="
				+ activity + ", issueTrackingReference=" + issueTrackingReference + ", invoice=" + invoice + ")";
	}

	
}
