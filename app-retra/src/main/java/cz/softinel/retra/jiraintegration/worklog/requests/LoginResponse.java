/**
 * 
 */
package cz.softinel.retra.jiraintegration.worklog.requests;

/**
 * Response to the login attempt.
 * @author andy
 * @see LoginRequest
 */
public class LoginResponse {
	/**
	 * This token is given by Jira after a successfull login.
	 */
	private String token;

	/**
	 * @return
	 * @see #token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 * @see #token
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
