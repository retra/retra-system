package cz.softinel.retra.jiraintegration;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.softinel.retra.worklog.blo.WorklogLogic;
import cz.softinel.retra.worklog.dao.WorklogFilter;
import cz.softinel.uaf.filter.FilterHelper;

public class JiraIssuesRefreshJob {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private JiraConfig jiraConfig;
	private WorklogLogic worklogLogic;

	public JiraConfig getJiraConfig() {
		return jiraConfig;
	}

	public void setJiraConfig(JiraConfig jiraConfig) {
		this.jiraConfig = jiraConfig;
	}

	public WorklogLogic getWorklogLogic() {
		return worklogLogic;
	}

	public void setWorklogLogic(WorklogLogic worklogLogic) {
		this.worklogLogic = worklogLogic;
	}

	public void executeJobs() {
		long start = System.currentTimeMillis();
		logger.info("Start JiraIssuesRefreshJob: " + start);
		if (jiraConfig.isEnabled()) {
			if (jiraConfig.getJiraCache() != null) {
				logger.info("Jira enabled and cache active");
				jiraConfig.getJiraCache().clearCache();

				// all items 2 months ago
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.MONTH, -2);
				cal.set(Calendar.DATE, 1);
				Date twoMonthAgo = cal.getTime();

				WorklogFilter filter = new WorklogFilter();
				FilterHelper.setField(WorklogFilter.WORKLOG_FILTER_FROM, twoMonthAgo, filter);

				worklogLogic.findByFilter(filter);
			}
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		logger.info("End JiraIssuesRefreshJob: " + end + " - duration: " + duration);
	}

}
