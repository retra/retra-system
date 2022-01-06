package cz.softinel.retra.jiraintegration.worklog;

/**
 * A helper class to work with {@link JiraWorklog}s.
 * 
 * @author SzalaiErik
 */
@Deprecated
public class JiraWorklogHelper {

	/**
	 * Create a {@link JiraRemoteWorklog} from a stored {@link JiraWorklog}.
	 * 
	 * @param jira
	 * @return
	 */
	public static JiraRemoteWorklog createRemoteWorklogFromJiraWorklog(JiraWorklog jira) {
		JiraRemoteWorklog jiraRemoteWorklog = new JiraRemoteWorklog();
		jiraRemoteWorklog.setAuthor(jira.getEmployee().getUser().getLogin().getName());
		jiraRemoteWorklog.setCreated(jira.getCreated());
		jiraRemoteWorklog.setTimeSpent(Math.round(jira.getTimeSpentInSeconds() / 60) + "m");
		jiraRemoteWorklog.setTimeSpentInSeconds(jira.getTimeSpentInSeconds());
		jiraRemoteWorklog.setComment(jira.getWorklog().getDescription());
		if (jira.getRemoteId() != null) {
			jiraRemoteWorklog.setId(String.valueOf(jira.getRemoteId()));
		}

		return jiraRemoteWorklog;
	}

}
