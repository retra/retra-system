package cz.softinel.retra.worklog.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.activity.blo.ActivityLogic;
import cz.softinel.retra.component.Component;
import cz.softinel.retra.component.blo.ComponentLogic;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.jiraintegration.JiraHelper;
import cz.softinel.retra.jiraintegration.JiraIssue;
import cz.softinel.retra.jiraintegration.logic.JiraLogic;
import cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.retra.worklog.blo.WorklogLogic;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 *
 */
public abstract class AbstractWorklogFormController extends FormController {
	
	private WorklogLogic worklogLogic;
	private ActivityLogic activityLogic;
	private ProjectLogic projectLogic;
	private ComponentLogic componentLogic;
	private JiraWorklogLogic jiraWorklogLogic;
	private InvoiceLogic invoiceLogic;
	
	private JiraLogic jiraLogic;
	
	/**
	 * Use this as a switch for UI elements. If the JiraIntegration is not enabled, set this to false and
	 * the Jira issue field will not be available.
	 * @author Erik Szalai
	 */
	@Deprecated
	private boolean jiraIntegrationEnabled = false;
	
	/**
	 * Get the integration switch
	 * @see #jiraIntegrationEnabled
	 */
	public boolean isJiraIntegrationEnabled() {
		return jiraIntegrationEnabled;
	}

	/**
	 * Set the integration switch.
	 * @param jiraIntegrationEnabled
	 * @see #jiraIntegrationEnabled
	 */
	public void setJiraIntegrationEnabled(boolean jiraIntegrationEnabled) {
		this.jiraIntegrationEnabled = jiraIntegrationEnabled;
	}
	
	/**
	 * @return
	 */
	public JiraWorklogLogic getJiraWorklogLogic() {
		return jiraWorklogLogic;
	}

	/**
	 * @param jiraWorklogLogic
	 */
	public void setJiraWorklogLogic(JiraWorklogLogic jiraWorklogLogic) {
		this.jiraWorklogLogic = jiraWorklogLogic;
	}

	public JiraLogic getJiraLogic() {
		return jiraLogic;
	}

	public void setJiraLogic(JiraLogic jiraLogic) {
		this.jiraLogic = jiraLogic;
	}

	/**
	 * @return the worklogLogic
	 */
	public WorklogLogic getWorklogLogic() {
		return worklogLogic;
	}

	/**
	 * @param worklogLogic the worklogLogic to set
	 */
	public void setWorklogLogic(WorklogLogic worklogLogic) {
		this.worklogLogic = worklogLogic;
	}

	/**
	 * @return the activityLogic
	 */
	public ActivityLogic getActivityLogic() {
		return activityLogic;
	}

	/**
	 * @param activityLogic the activityLogic to set
	 */
	public void setActivityLogic(ActivityLogic activityLogic) {
		this.activityLogic = activityLogic;
	}

	/**
	 * @return the projectLogic
	 */
	public ProjectLogic getProjectLogic() {
		return projectLogic;
	}

	/**
	 * @param projectLogic the projectLogic to set
	 */
	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}
	
	/**
	 * @return the componentLogic
	 */
	public ComponentLogic getComponentLogic() {
		return componentLogic;
	}

	/**
	 * @param componentLogic the componentLogic to set
	 */
	public void setComponentLogic(ComponentLogic componentLogic) {
		this.componentLogic = componentLogic;
	}

	public InvoiceLogic getInvoiceLogic() {
		return invoiceLogic;
	}

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	protected void prepareWorklogForm(WorklogForm worklogForm, RequestContext requestContext) {
		getCookieHelper().importFromCookies(worklogForm, requestContext);
		if (requestContext.getParameter("setDate") != null) {
			worklogForm.setDate(requestContext.getParameter("setDate"));
		}
		if (requestContext.getParameter("setWorkFrom") != null) {
			worklogForm.setWorkFrom(requestContext.getParameter("setWorkFrom"));
		}
		if (requestContext.getParameter("setWorkTo") != null) {
			worklogForm.setWorkTo(requestContext.getParameter("setWorkTo"));
		}
	}

	protected void prepareActivities(Model model) {
		prepareActivities(model, false);
	}

	protected void prepareActivities(Model model, boolean showAll) {
		List<Activity> activities = null;
		if (showAll) {
			activities = activityLogic.findAllActivities();
		}
		else {
			activities = activityLogic.findAllNotDeletedActivities();			
		}

		model.put("activities", activities);
	}

	protected void prepareProjects(Model model) {
		prepareProjects(model, false);
	}

	protected void prepareProjects(Model model, boolean showAll) {
		List<Project> projects = null;
		if (showAll) {
			//projects = projectLogic.findAllProjects();
			projects = filterActiveProject(getSecurityLogic().getLoggedEmployee().getProjects());
		}
		else {
			//projects = projectLogic.findAllNotDeletedProjects();	
			projects = filterActiveProject(getSecurityLogic().getLoggedEmployee().getProjects());
		}

		model.put("projects", projects);
	}
	
	protected void prepareJiraIssues(Model model) {
		String ldapLogin = getSecurityLogic().getLoggedUser().getLogin().getLdapLogin();
		List<JiraIssue> issues = jiraLogic.findJiraIssuesForUser(ldapLogin);
		if (issues != null && !issues.isEmpty()) {
			for (JiraIssue ji : issues) {
				ji.setGuiLink(JiraHelper.getLinkableText(ji.getKey(), "(Show...)", jiraLogic.getJiraConfig()));
			}
		}
		model.put("issues", issues);
	}
	
	private List<Project> filterActiveProject(Set<Project> projects) {
		List<Project> filtersList=new ArrayList<Project>();
		for (Project p:projects) {
			if (p.getState()==Project.STATE_ACTIVE) {
				filtersList.add(p);
			}
		}
		return filtersList;
	}
	
	protected void prepareComponents(Model model) {
		prepareComponents(model, false);
	}

	protected void prepareComponents(Model model, boolean showAll) {
		List<Component> components = null;
		if (showAll) {
			components = componentLogic.findAllComponents();
		}
		else {
			components = componentLogic.findAllNotDeletedComponents();			
		}

		model.put("components", components);
	}

	protected void prepareInvoicesForCreateOrEdit(Model model) {
		List<Invoice> invoices = invoiceLogic.findAllActiveInvoicesForEmployee(getSecurityLogic().getLoggedEmployee().getPk());
		model.put("invoices", invoices);
	}


}
