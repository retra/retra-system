package cz.softinel.retra.jiraintegration.worklog.requests;

/**
 * Request object for getting an issue. Used mainly for issue name validation purposes before other operations.
 * @author SzalaiErik
 */
@Deprecated
public class GetIssueRequest {
	/**
	 * The session token got after a login.
	 */
	private String token;
	/**
	 * The name of the issue, not the id. Examples: "TST-15 RTR-3"
	 */
	private String issue;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}

}
