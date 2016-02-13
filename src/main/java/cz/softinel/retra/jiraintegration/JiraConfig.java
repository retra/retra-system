package cz.softinel.retra.jiraintegration;

public class JiraConfig {

	private boolean enabled;
	private String user;
	private String password;
	private String baseUrl;
	private String issuePath;
	private String restPath;
	
	private JiraCache jiraCache;

	public JiraCache getJiraCache() {
		return jiraCache;
	}

	public void setJiraCache(JiraCache jiraCache) {
		this.jiraCache = jiraCache;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getIssuePath() {
		return issuePath;
	}

	public void setIssuePath(String issuePath) {
		this.issuePath = issuePath;
	}

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}

}
