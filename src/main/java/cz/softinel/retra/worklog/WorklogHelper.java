package cz.softinel.retra.worklog;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.component.Component;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.web.WorklogForm;
import cz.softinel.uaf.util.StringHelper;

/**
 * This class contains all helper methods for worklog.
 *
 * @version $Revision: 1.7 $ $Date: 2007-07-24 17:43:41 $
 * @author Petr Sígl
 */
public class WorklogHelper {
	
	public static final String HELP_COOKIE_PARAM_ADD_ALSO_FROM = "addAlsoFrom";
	
	public static void formToEntity(WorklogForm form, Worklog entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		
		Date from = DateConvertor.getDateFromDateStringHourString(form.getDate(), form.getWorkFrom());
		entity.setWorkFrom(from);

		Date to = DateConvertor.getDateFromDateStringHourString(form.getDate(), form.getWorkTo());
		entity.setWorkTo(to);

		entity.setDescription(form.getDescription());
		
		Activity activity = entity.getActivity();
		if (activity == null) {
			activity = new Activity();
		}
		entity.setActivity(activity);
		Long activityPk = LongConvertor.getLongFromString(form.getActivity());
		activity.setPk(activityPk);
		
		Project project = entity.getProject();
		if (project == null) {
			project = new Project();
		}
		entity.setProject(project);
		Long projectPk = LongConvertor.getLongFromString(form.getProject());
		project.setPk(projectPk);
		
		final Long componentPk = LongConvertor.getLongFromString(form.getComponent());
		if (componentPk == null) {
			entity.setComponent(null);
		} else {
			Component component = entity.getComponent();
			if (component == null) {
				component = new Component();
			}
			entity.setComponent(component);
			component.setPk(componentPk);
		}
		
		final Long invoicePk = LongConvertor.getLongFromString(form.getInvoice());
		if (invoicePk == null) {
			entity.setInvoice(null);
		} else {
			Invoice invoice = entity.getInvoice();
			if (invoice == null) {
				invoice = new Invoice();
			}
			entity.setInvoice(invoice);
			invoice.setPk(invoicePk);
		}
		
		if (entity.hasAnyIssueTrackingWorklog() || StringHelper.notEmpty(form.getIssueTrackingReference())) {
			JiraWorklog jiraWorklog = new JiraWorklog();
			
			jiraWorklog.setCreated(entity.getWorkFrom());
			jiraWorklog.setJiraIssue(form.getIssueTrackingReference());
			jiraWorklog.setState(JiraWorklog.CREATED); 
			jiraWorklog.setTimeSpentInSeconds(entity.getHours().multiply(new BigDecimal(3600)).longValue()); // TODO Zoli, not exact

			entity.addIssueTrackingWorklog(jiraWorklog);
		} 
	}
	
	public static void formToEntityUpdate(WorklogForm form, Worklog entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		
		Date from = DateConvertor.getDateFromDateStringHourString(form.getDate(), form.getWorkFrom());
		entity.setWorkFrom(from);

		Date to = DateConvertor.getDateFromDateStringHourString(form.getDate(), form.getWorkTo());
		entity.setWorkTo(to);

		entity.setDescription(form.getDescription());
		
		Activity activity = entity.getActivity();
		if (activity == null) {
			activity = new Activity();
		}
		entity.setActivity(activity);
		Long activityPk = LongConvertor.getLongFromString(form.getActivity());
		activity.setPk(activityPk);
		
		Project project = entity.getProject();
		if (project == null) {
			project = new Project();
		}
		entity.setProject(project);
		Long projectPk = LongConvertor.getLongFromString(form.getProject());
		project.setPk(projectPk);
		
		final Long componentPk = LongConvertor.getLongFromString(form.getComponent());
		if (componentPk == null) {
			entity.setComponent(null);
		} else {
			Component component = entity.getComponent();
			if (component == null) {
				component = new Component();
			}
			entity.setComponent(component);
			component.setPk(componentPk);
		}

		final Long invoicePk = LongConvertor.getLongFromString(form.getInvoice());
		if (invoicePk == null) {
			entity.setInvoice(null);
		} else {
			Invoice invoice = entity.getInvoice();
			if (invoice == null) {
				invoice = new Invoice();
			}
			entity.setInvoice(invoice);
			invoice.setPk(invoicePk);
		}
		
		if (StringHelper.isEmpty(form.getIssueTrackingReference()) && entity.hasAnyIssueTrackingWorklog()) {
			//The reference was set empty, the JiraWorklog must be deleted. A job deletes it, the worklog must be null.
			JiraWorklog jiraWorklog = entity.getCurrentIssueTrackingWorklog();
			jiraWorklog.setWorklog(null);
		} else if (entity.hasAnyIssueTrackingWorklog() || StringHelper.notEmpty(form.getIssueTrackingReference())) {
			Set<JiraWorklog> jiraWorklogs = entity.getIssueTrackingWorklogs();
			JiraWorklog jiraWorklog = null;
			if (jiraWorklogs.size() == 1) {
				jiraWorklog = jiraWorklogs.iterator().next();
				jiraWorklog.setCreated(entity.getWorkFrom());
				jiraWorklog.setTimeSpentInSeconds(entity.getHours().multiply(new BigDecimal(3600)).longValue());
				if (jiraWorklog.getJiraIssue().equals(form.getIssueTrackingReference())) {
					jiraWorklog.setState(JiraWorklog.UPDATED); 
				} else {
					jiraWorklog.setState(JiraWorklog.ISSUE_UPDATED); 
				}
				jiraWorklog.setJiraIssue(form.getIssueTrackingReference());
				
			} else if(jiraWorklogs.size() == 0) {
				jiraWorklog = new JiraWorklog();
				jiraWorklog.setCreated(entity.getWorkFrom());
				jiraWorklog.setJiraIssue(form.getIssueTrackingReference());
				jiraWorklog.setState(JiraWorklog.CREATED); 
				jiraWorklog.setTimeSpentInSeconds(entity.getHours().multiply(new BigDecimal(3600)).longValue());
				entity.addIssueTrackingWorklog(jiraWorklog);
			}
		} 
	}

	public static void entityToForm(Worklog entity, WorklogForm form) {
		String pk = LongConvertor.convertToStringFromLong(entity.getPk());
		form.setPk(pk);
		
		String date = DateConvertor.convertToDateStringFromDate(entity.getWorkFrom());
		form.setDate(date);
		
		String from = DateConvertor.convertToHourStringFromDate(entity.getWorkFrom());
		form.setWorkFrom(from);
		
		String to = DateConvertor.convertToHourStringFromDate(entity.getWorkTo());
		// HACK radek: AWIS-40 / Edit error when worklog end with 00:00 
		if ("00:00".equals(to)) {
			to = "24:00";
		}
		form.setWorkTo(to);
		
		form.setDescription(entity.getDescription());
		
		Activity activity = entity.getActivity();
		if (activity != null) {
			String activityPk = LongConvertor.convertToStringFromLong(entity.getActivity().getPk());
			form.setActivity(activityPk);
		}

		Project project = entity.getProject();
		if (project != null) {
			String projectPk = LongConvertor.convertToStringFromLong(entity.getProject().getPk());
			form.setProject(projectPk);
		}
		
		Component component = entity.getComponent();
		if (component != null) {
			String componentPk = LongConvertor.convertToStringFromLong(entity.getComponent().getPk());
			form.setComponent(componentPk);
		}

		Invoice invoice = entity.getInvoice();
		if (invoice != null) {
			String invoicePk = LongConvertor.convertToStringFromLong(entity.getInvoice().getPk());
			form.setInvoice(invoicePk);
		}

		if (entity.hasAnyIssueTrackingWorklog()) {
			form.setIssueTrackingReference(entity.getCurrentIssueTrackingWorklog().getJiraIssue());
		}
	}
}
