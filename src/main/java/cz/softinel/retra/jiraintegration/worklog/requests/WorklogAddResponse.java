package cz.softinel.retra.jiraintegration.worklog.requests;

/**
 * Response to the worklog adding.
 * @author Erik Szalai
 */
public class WorklogAddResponse {
	
private String success;
	
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getSuccess() {
		return success;
	}

}
