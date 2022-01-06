package cz.softinel.retra.worklog.web;

/**
 * Command object for the Worklog Invoice pair
 * 
 * @author petr
 */
public class WorklogInvoicePairForm {

	private Long[] confirmedItems;

	private String invoice;
	private String project;
	private String activity;
	private String dateFrom;
	private String dateTo;

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public Long[] getConfirmedItems() {
		return confirmedItems;
	}

	public void setConfirmedItems(Long[] confirmedItems) {
		this.confirmedItems = confirmedItems;
	}

	@Override
	public String toString() {
		return "(WorklogInvoicePairForm: invoice=" + invoice + ", project=" + project + ", activity=" + activity
				+ ", dateFrom=" + dateFrom + ", dateTo=" + dateTo + ")";
	}
	
}
