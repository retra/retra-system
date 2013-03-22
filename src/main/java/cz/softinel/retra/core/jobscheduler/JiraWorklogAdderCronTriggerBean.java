package cz.softinel.retra.core.jobscheduler;

import org.springframework.scheduling.quartz.CronTriggerBean;

import cz.softinel.retra.jiraintegration.JiraIntegrationJobs;

/**
 * This class is a cron-like configurable Scheduler
 * 
 * @author Zoltan Vadasz
 * @deprecated by Erik Szalai. The job can be switched off in {@link JiraIntegrationJobs}.
 */
public class JiraWorklogAdderCronTriggerBean extends CronTriggerBean {

	private static final long serialVersionUID = 1L;
	
	// TODO Zoli: somehow solve enable/disable of jobs 
	private boolean enabled;
	
	// Getters and setters
	
	/**
	 * @return true if it's enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param set if this should be enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
