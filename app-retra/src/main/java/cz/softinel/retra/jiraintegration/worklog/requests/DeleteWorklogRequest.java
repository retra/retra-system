package cz.softinel.retra.jiraintegration.worklog.requests;

/**
 * This request class is used to delete worklogs.
 * @author SzalaiErik
 */
public class DeleteWorklogRequest {
	private String loginToken;
	private String issueId;
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

}
