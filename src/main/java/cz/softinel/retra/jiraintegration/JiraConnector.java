package cz.softinel.retra.jiraintegration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import cz.softinel.uaf.ssl.SSLTool;

public class JiraConnector implements InitializingBean {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private SimpleDateFormat jiraDateFormat;

	private JiraConfig jiraConfig;
	private Client client;

	public JiraConfig getJiraConfig() {
		return jiraConfig;
	}

	public void setJiraConfig(JiraConfig jiraConfig) {
		this.jiraConfig = jiraConfig;
	}

	public void afterPropertiesSet() throws Exception {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		client = Client.create(config);
		client.addFilter(new HTTPBasicAuthFilter(jiraConfig.getUser(), jiraConfig.getPassword()));

		// siglp: hack
		SSLTool.disableCertificateValidation();

		// date format
		jiraDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}

	public List<JiraIssue> findIssuesForWorklog(final String loginName) {
		String jql = "status NOT IN (Done, Closed) AND assignee =" + loginName + " OR (watcher = " + loginName
				+ " AND labels in (STANDUP, CONSULT, ADMIN))";
		List<JiraIssue> result = findIssues(jql);
		return result;
	}

	public JiraIssue getJiraIssue(final String issueKey) {
		try {
			final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey
					+ "?fields=id,key,summary";
			final ClientResponse response = client.resource(url).accept(MediaType.APPLICATION_JSON_TYPE)
					.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				return null;
			}
			final JiraIssue result = response.getEntity(JiraIssue.class);
			return result;
		} catch (Exception e) {
			logger.error("Problem with Jira connect.", e);
			return null;
		}
	}

	public boolean addWorklog(final String issueKey, final Date started, final long duration, final String loginName,
			final String comment, final Long worklogPk) {
		try {
			final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog";
			final String data = getAddWorklogString(issueKey, started, duration, loginName, comment, null, worklogPk);
			final ClientResponse response = client.resource(url).accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_JSON_TYPE).header("sudo", loginName).post(ClientResponse.class, data);
			if (response.getStatus() != 201) {
				logger.error("Couldn't write worklog for user: " + loginName + ", response status code: "
						+ response.getStatus());
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("Problem with Jira connect.", e);
			return false;
		}
	}

	public boolean updateWorklog(final String issueKey, final Date started, final long duration, final String loginName,
			final String comment, final String id, final Long worklogPk) {
		try {
			final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog/"
					+ id;
			final String data = getAddWorklogString(issueKey, started, duration, loginName, comment, id, worklogPk);
			final ClientResponse response = client.resource(url).accept(MediaType.APPLICATION_JSON_TYPE)
					.type(MediaType.APPLICATION_JSON_TYPE).header("sudo", loginName).put(ClientResponse.class, data);
			if (response.getStatus() != 200) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("Problem with Jira connect.", e);
			return false;
		}
	}

	public boolean deleteWorklog(final String issueKey, final String id) {
		try {
			final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog/"
					+ id;
			final ClientResponse response = client.resource(url).accept(MediaType.APPLICATION_JSON_TYPE)
					.delete(ClientResponse.class);
			if (response.getStatus() != 204) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("Problem with Jira connect.", e);
			return false;
		}
	}

	public String findWorklogId(final String issueKey, final Date started, final long duration, final String loginName,
			final String comment) {
		try {
			final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog";
			final ClientResponse response = client.resource(url).accept(MediaType.APPLICATION_JSON_TYPE)
					.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				return null;
			}
			final JiraWorklogsResult searchResult = response.getEntity(JiraWorklogsResult.class);
			final List<JiraWorklog> worklogs = searchResult.getWorklogs();

			String result = null;
			if (worklogs != null && !worklogs.isEmpty()) {
				for (JiraWorklog jw : worklogs) {
					try {
						if (jw.getAuthor() != null && jw.getAuthor().getName() != null && jw.getStarted() != null
								&& jw.getTimeSpentSeconds() != null) {

							Date startedDate = jiraDateFormat.parse(jw.getStarted());
							long timeSpent = Long.parseLong(jw.getTimeSpentSeconds());
							String author = jw.getAuthor().getName();

							if (startedDate.equals(started) && timeSpent == duration && author.equals(loginName)) {

								result = jw.getId();
								break;
							}
						}
					} catch (Exception e) {
						logger.error("Problem with Jira connect.", e);
					}
				}
			}

			return result;
		} catch (Exception e) {
			logger.error("Problem with Jira connect.", e);
			return null;
		}
	}

	private List<JiraIssue> findIssues(final String query) {
		try {
			final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath()
					+ "search?fields=id,key,summary&maxResults=100&jql=" + urlEncode(query);
			final ClientResponse response = client.resource(url).accept(MediaType.APPLICATION_JSON_TYPE)
					.get(ClientResponse.class);
			if (response.getStatus() != 200) {
				return null;
			}
			final JiraSearchResult searchResult = response.getEntity(JiraSearchResult.class);
			final List<JiraIssue> result = searchResult.getIssues();
			return result;
		} catch (Exception e) {
			logger.error("Problem with Jira connect.", e);
			return null;
		}
	}

	private String urlEncode(final String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}

	protected String buildJiraComment(final Long worklogPk, final String loginName, final String clearComment) {
		final StringBuilder result = new StringBuilder();
//		result.append(loginName);
//		result.append(" - auto from Retra: ");
//		result.append(clearComment);
		result.append("Retra log #");
		result.append(worklogPk);
		result.append(" by @");
		result.append(loginName);
		result.append(": ");
		result.append(clearComment);
		return result.toString();
	}

	private String getAddWorklogString(final String issueKey, final Date started, final long duration,
			final String loginName, final String comment, final String id, final Long worklogPk) {
		String startedStr = jiraDateFormat.format(started);
		String clearComment = clearComment(comment);
		String jiraComment = buildJiraComment(worklogPk, loginName, clearComment);
		StringBuilder sb = new StringBuilder();
		sb.append("{\"author\": {\"name\": \"").append(loginName).append("\"}, ");
		sb.append("\"comment\": \"").append(jiraComment).append("\", ");
		sb.append("\"started\": \"").append(startedStr).append("\", ");
		sb.append("\"timeSpentSeconds\": \"").append(duration).append("\"");
		if (id != null) {
			sb.append(", \"id\": \"").append(id).append("\"");
		}
		sb.append("}");
		return sb.toString();
	}

	private String clearComment(final String old) {
		String result = "";
		if (old != null) {
			result = old.trim();
			result = result.replaceAll("\t", " ");
			result = result.replaceAll("\n", " ");
			result = result.replaceAll("\r", " ");
			result = result.replaceAll("\\s+", " ");
			result = StringEscapeUtils.escapeJava(result).trim();
		}
		return result;
	}

}
