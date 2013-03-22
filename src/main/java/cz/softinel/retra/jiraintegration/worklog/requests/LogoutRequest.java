package cz.softinel.retra.jiraintegration.worklog.requests;
/**
 * Logout attempt.
 * @author zoli
 */
public class LogoutRequest {

	private String loginToken;
	
	public String getLoginToken() {
		return loginToken;
	}
	
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	
}
