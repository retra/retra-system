package cz.softinel.retra.jiraintegration.worklog.requests;

/**
 * Response to logout.
 */
@Deprecated
public class LogoutResponse {
	
	private String success;
	
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getSuccess() {
		return success;
	}

}
