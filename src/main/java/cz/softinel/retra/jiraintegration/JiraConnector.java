package cz.softinel.retra.jiraintegration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.InitializingBean;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import cz.softinel.uaf.ssl.SSLTool;

public class JiraConnector implements InitializingBean {

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
		
		//siglp: hack
		SSLTool.disableCertificateValidation();
		
		//date format
		jiraDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	}
	
	public List<JiraIssue> findIssuesForWorklog(final String loginName) {
		String jql = "status != Closed AND assignee =" + loginName;
		List<JiraIssue> result = findIssues(jql);
		return result;
	}

	public boolean addWorklog(final String issueKey, final Date started, final long duration, final String loginName, final String comment) {
		final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog";
		final String data = getAddWorklogString(issueKey, started, duration, loginName, comment, null);
		final ClientResponse response = client.resource(url)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)		
				.header("sudo", loginName)
				.post(
						ClientResponse.class,
						data
				);
		if (response.getStatus() == 400) {
			return false;
		}
		if (response.getStatus() != 201) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " / ");
		}
		return true;
	}
	
	public boolean updateWorklog(final String issueKey, final Date started, final long duration, final String loginName, final String comment, final String id) {
		final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog/" + id;
		final String data = getAddWorklogString(issueKey, started, duration, loginName, comment, id);
		final ClientResponse response = client.resource(url)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE)		
				.header("sudo", loginName)
				.put(
						ClientResponse.class,
						data
				);
		if (response.getStatus() == 400) {
			return false;
		}
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " / ");
		}
		return true;
	}

	public boolean deleteWorklog(final String issueKey, final String id) {
		final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog/" + id;
		final ClientResponse response = client.resource(url)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.delete(
						ClientResponse.class
				);
		if (response.getStatus() == 400) {
			return false;
		}
		if (response.getStatus() != 204) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " / ");
		}
		return true;
	}
	
	public String findWorklogId(final String issueKey, final Date started, final long duration, final String loginName, final String comment) {
		final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "issue/" + issueKey + "/worklog";
		final ClientResponse response = client.resource(url)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(ClientResponse.class);
		if (response.getStatus() == 400) {
			return null;
		}
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " / ");
		}
		final JiraWorklogsResult searchResult = response.getEntity(JiraWorklogsResult.class);
		final List<JiraWorklog> worklogs = searchResult.getWorklogs();

		String result = null;
		if (worklogs != null && !worklogs.isEmpty()) {
			for (JiraWorklog jw : worklogs) {
				try {
					if (jw.getAuthor() != null && jw.getAuthor().getName() != null
						&& jw.getStarted() != null
						&& jw.getTimeSpentSeconds() != null) {
						
						Date startedDate = jiraDateFormat.parse(jw.getStarted());
						long timeSpent = Long.parseLong(jw.getTimeSpentSeconds());
						String author = jw.getAuthor().getName();
						
						if (startedDate.equals(started)
							&& timeSpent == duration
							&& author.equals(loginName)) {

							result = jw.getId();
							break;
						}
					}
				} catch (Exception e) {
					//TODO:
				}
			}
		}
		
		return result;
	}
	
	private List<JiraIssue> findIssues(final String query) {
		final String url = jiraConfig.getBaseUrl() + jiraConfig.getRestPath() + "search?fields=id,key,summary&maxResults=100&jql=" + urlEncode(query);
		final ClientResponse response = client.resource(url)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get(ClientResponse.class);
		if (response.getStatus() == 400) {
			return null;
		}
		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus() + " / " + query);
		}
		final JiraSearchResult searchResult = response.getEntity(JiraSearchResult.class);
		final List<JiraIssue> result = searchResult.getIssues();
		return result;
	}
	
	private String urlEncode(final String string) {
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}

	private String getAddWorklogString(final String issueKey, final Date started, final long duration, final String loginName, final String comment, final String id) {
		String startedStr = jiraDateFormat.format(started); 
		StringBuilder sb = new StringBuilder();
		sb.append("{\"author\": {\"name\": \"").append(loginName).append("\"}, ");
		sb.append("\"comment\": \"").append(loginName).append(" - auto from Retra: ").append(comment).append("\", ");
		sb.append("\"started\": \"").append(startedStr).append("\", ");
		sb.append("\"timeSpentSeconds\": \"").append(duration).append("\"");
		if (id != null) {
			sb.append(", \"id\": \"").append(id).append("\"");
		}
		sb.append("}");
		return sb.toString();
	}
}
