/**
 * 
 */
package cz.softinel.retra.worklog.web;

import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.project.Project;

/**
 * @author milan.cecrdle
 *
 */
public class ProjectInvoiceHolder {
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	Project project;
	Invoice invoice;
}
