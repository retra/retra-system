package cz.softinel.retra.jiraintegration.soap;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.AbstractMarshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

import cz.softinel.retra.jiraintegration.worklog.JiraRemoteWorklog;
import cz.softinel.retra.jiraintegration.worklog.JiraWorklog;
import cz.softinel.retra.jiraintegration.worklog.JiraWorklogHelper;
import cz.softinel.retra.jiraintegration.worklog.requests.DeleteWorklogRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.GetIssueRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.LoginRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.LoginResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.LogoutRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.LogoutResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogAddRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogAddResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogUpdateRequest;
import cz.softinel.retra.jiraintegration.worklog.service.JiraWorklogLogic;
import cz.softinel.uaf.util.StringHelper;

/**
 * Implementation of the SOAP client interface.
 * 
 * @author Erik Szalai
 * @see JiraSoapClient
 * @see JiraSoapClientMarshaller
 */
@Deprecated
public class JiraSoapClientImpl implements JiraSoapClient {

	private JiraWorklogLogic jiraWorklogLogic;
	private String jiraMasterPassword;
	private boolean useLdapLogin;
	private WebServiceTemplate webService;

	static Logger logger = LoggerFactory.getLogger(JiraSoapClientImpl.class);

	/**
	 * Set the JiraWorklog logic manager.
	 * 
	 * @param jiraWorklogLogic
	 */
	public void setJiraWorklogLogic(JiraWorklogLogic jiraWorklogLogic) {
		this.jiraWorklogLogic = jiraWorklogLogic;
	}

	/**
	 * Set the master password. This needs revision.
	 * 
	 * @param jiraMasterPassword
	 */
	public void setJiraMasterPassword(String jiraMasterPassword) {
		this.jiraMasterPassword = jiraMasterPassword;
	}

	/**
	 * Set this to true, if during the Jira integration login process the Ldap login
	 * of the User shall be used instead of the normal login.
	 * 
	 * @param useLdapLogin
	 */
	public void setUseLdapLogin(boolean useLdapLogin) {
		this.useLdapLogin = useLdapLogin;
	}

	/**
	 * Use this constructor to ensure a Jira SOAP service url is always given.
	 * 
	 * @param jiraSoapService
	 * @param marshaller      The marshaller, which will marshall XML <-> Object.
	 *                        Use {@link JiraSoapClientMarshaller}
	 * @see JiraSoapClientMarshaller
	 */
	public JiraSoapClientImpl(String jiraSoapService, AbstractMarshaller marshaller) {
		StringHelper.notEmpty(jiraSoapService);
		webService = new WebServiceTemplate();
		webService.setMarshaller(marshaller);
		webService.setUnmarshaller(marshaller);
		webService.setDefaultUri(jiraSoapService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.softinel.retra.jiraintegration.soap.JiraSoapClient#exportInserted()
	 */
	public void exportInserted() {
		try {
			List<JiraWorklog> jiraWorklogs = jiraWorklogLogic.findAllNotAdded();
			for (JiraWorklog jira : jiraWorklogs) {
				JiraRemoteWorklog jiraRemoteWorklog = JiraWorklogHelper.createRemoteWorklogFromJiraWorklog(jira);

				String username = null;
				if (useLdapLogin) {
					username = jira.getEmployee().getUser().getLogin().getLdapLogin();
				} else {
					username = jira.getEmployee().getUser().getLogin().getName();
				}
				String sessionToken = login(username, jiraMasterPassword);
				if (sessionToken != null) {
					if (issueExists(sessionToken, jira.getJiraIssue())) {
						String success = addWorklogAndAutoAdjustRemainingEstimate(sessionToken, jira.getJiraIssue(),
								jiraRemoteWorklog);
						jira.setRemoteId(Long.parseLong(success));
						jira.setState(JiraWorklog.EXPORTED);
						jiraWorklogLogic.update(jira);
					} else {
						jira.setState(JiraWorklog.EXPORTED_FOR_NONEXISTENT);
						jiraWorklogLogic.update(jira);
					}
					logout(sessionToken);
				}
			}
		} catch (Exception e) {
			logger.error("Exception happened while executing Jira insert: ", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.softinel.retra.jiraintegration.soap.JiraSoapClient#exportUpdated()
	 */
	public void exportUpdated() {
		try {
			List<JiraWorklog> jiraWorklogs = jiraWorklogLogic.findAllNotAddedUpdated();
			logger.debug("Found JiraWorklogs: " + jiraWorklogs.size());
			for (JiraWorklog jira : jiraWorklogs) {
				logger.debug("Working with " + jira.getJiraIssue() + " and the Retra worklog related is: "
						+ jira.getWorklog().getDescription());
				JiraRemoteWorklog jiraRemoteWorklog = JiraWorklogHelper.createRemoteWorklogFromJiraWorklog(jira);

				String username = null;
				if (useLdapLogin) {
					username = jira.getEmployee().getUser().getLogin().getLdapLogin();
				} else {
					username = jira.getEmployee().getUser().getLogin().getName();
				}
				String sessionToken = login(username, jiraMasterPassword);
				if (sessionToken != null) {
					if (!issueExists(sessionToken, jira.getJiraIssue())) {
						logger.warn("The issue does not exist: " + jira.getJiraIssue());
					}
					if (jira.getRemoteId() != null) {
						updateWorklogAndAutoAdjustRemainingEstimate(sessionToken, jiraRemoteWorklog);
						jira.setState(JiraWorklog.EXPORTED);
						jiraWorklogLogic.update(jira);
					} else {
						String success = addWorklogAndAutoAdjustRemainingEstimate(sessionToken, jira.getJiraIssue(),
								jiraRemoteWorklog);
						logger.debug("Sucess: " + success);
						jira.setRemoteId(Long.valueOf(success));
						jira.setState(JiraWorklog.EXPORTED);
						jiraWorklogLogic.update(jira);
					}
					logout(sessionToken);
				}
			}
		} catch (Exception e) {
			logger.error("Exception happened while executing Jira update: " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.softinel.retra.jiraintegration.soap.JiraSoapClient#exportWithIssueUpdated(
	 * )
	 */
	public void exportWithIssueUpdated() {
		try {
			List<JiraWorklog> jiraWorklogs = jiraWorklogLogic.findAllWithIssueUpdated();
			for (JiraWorklog jira : jiraWorklogs) {
				JiraRemoteWorklog jiraRemoteWorklog = JiraWorklogHelper.createRemoteWorklogFromJiraWorklog(jira);

				String username = null;
				if (useLdapLogin) {
					username = jira.getEmployee().getUser().getLogin().getLdapLogin();
				} else {
					username = jira.getEmployee().getUser().getLogin().getName();
				}
				String sessionToken = login(username, jiraMasterPassword);
				if (sessionToken != null) {
					String success = "";
					if (!issueExists(sessionToken, jira.getJiraIssue())) {
						logger.warn("The issue does not exist: " + jira.getJiraIssue());
						if (jira.getRemoteId() != null) {
							deleteWorklogAndAutoAdjustRemainingEstimate(sessionToken, jiraRemoteWorklog.getId());
							jira.setRemoteId(null);
						}
						jira.setState(JiraWorklog.EXPORTED_FOR_NONEXISTENT);
						jiraWorklogLogic.update(jira);
					} else {
						if (jira.getRemoteId() != null) {
							deleteWorklogAndAutoAdjustRemainingEstimate(sessionToken, jiraRemoteWorklog.getId());
						}
						success = addWorklogAndAutoAdjustRemainingEstimate(sessionToken, jira.getJiraIssue(),
								jiraRemoteWorklog);
						jira.setRemoteId(Long.valueOf(success));
						jira.setState(JiraWorklog.EXPORTED);
						jiraWorklogLogic.update(jira);
					}
					logout(sessionToken);
				}
			}
		} catch (Exception e) {
			logger.error("Exception happened while executing Jira issue update: " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.softinel.retra.jiraintegration.soap.JiraSoapClient#exportDeleted()
	 */
	public void exportDeleted() {
//		List<JiraWorklog> jiraWorklogs = jiraWorklogLogic.findAllJiraWorklogs();
		try {
			List<JiraWorklog> jiraWorklogs = jiraWorklogLogic.findAllToBeDeleted();
			for (JiraWorklog jira : jiraWorklogs) {
				if (jira.getWorklog() == null) {
					String username = null;
					if (useLdapLogin) {
						username = jira.getEmployee().getUser().getLogin().getLdapLogin();
					} else {
						username = jira.getEmployee().getUser().getLogin().getName();
					}
					String sessionToken = login(username, jiraMasterPassword);
					if (sessionToken != null) {
						if (jira.getRemoteId() != null) {
							deleteWorklogAndAutoAdjustRemainingEstimate(sessionToken,
									String.valueOf(jira.getRemoteId()));
							jiraWorklogLogic.delete(jira);
						}
					}
					logout(sessionToken);
				}
			}
		} catch (Exception e) {
			logger.error("Exception happened while executing Jira issue update: " + e);
		}
	}

	/**
	 * Logger in into Jira with the given username and password.
	 * 
	 * @param username
	 * @param password
	 * @return A "session string" which identificates the logged in user.
	 */
	public String login(String username, String password) {
		LoginRequest request = new LoginRequest();
		request.setUsername(username);
		request.setPassword(password);
		LoginResponse response;
		try {
			response = (LoginResponse) webService.marshalSendAndReceive(request);
		} catch (SoapFaultClientException sf) {
			logger.error(sf.getFaultStringOrReason() + " Username: " + username + " Password: " + password);
			return null;
		}
		return response.getToken();
	}

	/**
	 * Required logout from Jira.
	 * 
	 * @param loginToken
	 * @return
	 */
	public String logout(String loginToken) {
		LogoutRequest request = new LogoutRequest();
		request.setLoginToken(loginToken);
		LogoutResponse response = (LogoutResponse) webService.marshalSendAndReceive(request);
		return response.getSuccess();
	}

	/**
	 * Adds a worklog to the given issue in Jira.
	 * 
	 * @param loginToken
	 * @param issue
	 * @param jiraRemoteWorklog
	 * @return
	 */
	public String addWorklogAndAutoAdjustRemainingEstimate(String loginToken, String issue,
			JiraRemoteWorklog jiraRemoteWorklog) {
		WorklogAddRequest request = new WorklogAddRequest();
		request.setLoginToken(loginToken);
		request.setJiraIssue(issue);
		request.setJiraRemoteWorklog(jiraRemoteWorklog);
		try {
			WorklogAddResponse response = (WorklogAddResponse) webService.marshalSendAndReceive(request);
			return response.getSuccess();
		} catch (SoapFaultClientException sf) {
			logger.error(sf.getFaultStringOrReason() + " issue: " + issue);
			return null;
		}
	}

	/**
	 * Finds a worklog in jira and updates it by the given data.
	 * 
	 * @param loginToken
	 * @param issue
	 * @param jiraRemoteWorklog
	 * @return
	 */
	public String updateWorklogAndAutoAdjustRemainingEstimate(String loginToken, JiraRemoteWorklog jiraRemoteWorklog) {
		WorklogUpdateRequest request = new WorklogUpdateRequest();
		request.setLoginToken(loginToken);
		request.setJiraRemoteWorklog(jiraRemoteWorklog);
		try {
			webService.marshalSendAndReceive(request);
		} catch (SoapFaultClientException e) {
			logger.error(e.getFaultStringOrReason() + " worklog: " + jiraRemoteWorklog);
			return null;
		}
		return "success";
	}

	/**
	 * Deletes the given worklog from Jira.
	 * 
	 * @param loginToken
	 * @param worklogId
	 * @return
	 */
	public String deleteWorklogAndAutoAdjustRemainingEstimate(String loginToken, String worklogId) {
		DeleteWorklogRequest request = new DeleteWorklogRequest();
		request.setLoginToken(loginToken);
		request.setIssueId(worklogId);
		try {
			webService.marshalSendAndReceive(request);
		} catch (SoapFaultClientException sf) {
			logger.error(sf.getFaultStringOrReason() + " WorklogId: " + worklogId);
			return null;
		}
		return "success";
	}

	/**
	 * Checks if the issue exists in Jira.
	 * 
	 * @param loginToken
	 * @param issue
	 * @return
	 */
	public boolean issueExists(String loginToken, String issue) {
		GetIssueRequest req = new GetIssueRequest();
		req.setToken(loginToken);
		req.setIssue(issue);
		try {
			webService.marshalSendAndReceive(req);
		} catch (SoapFaultClientException sf) {
			logger.error(sf.getFaultStringOrReason() + " Issue: " + issue);
			return false;
		}
		return true;
	}
}
