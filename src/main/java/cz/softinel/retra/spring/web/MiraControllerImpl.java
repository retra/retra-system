package cz.softinel.retra.spring.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.uaf.spring.web.controller.Context;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class MiraControllerImpl implements MiraController {

	private MiraSecurityLogic securityLogic;
	private boolean requireLogin;
	private String loginPageAction;
	
	public String getLoginPageAction() {
		return loginPageAction;
	}

	public void setLoginPageAction(String loginPageAction) {
		this.loginPageAction = loginPageAction;
	}
	
	public boolean isRequireLogin() {
		return requireLogin;
	}
	
	public void setRequireLogin(boolean requireLogin) {
		this.requireLogin = requireLogin;
	}
	
	public MiraSecurityLogic getSecurityLogic() {
		return securityLogic;
	}
	
	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		this.securityLogic = securityLogic;
	}

	public void setShowHistoryData(boolean showHistoryData, RequestContext requestContext) {
		Context sessionContext = requestContext.getSessionContext();
		Boolean showHistoryDataBool = Boolean.valueOf(showHistoryData);
		sessionContext.setAttribute(MiraController.PARAM_NAME_SHOW_HISTORY_DATA, showHistoryDataBool);
	}
	
	public boolean getShowHistoryData(RequestContext requestContext) {
		Context sessionContext = requestContext.getSessionContext();
		Object showHistoryDataObj = sessionContext.getAttribute(MiraController.PARAM_NAME_SHOW_HISTORY_DATA);
		boolean result = false;
		if (showHistoryDataObj != null && showHistoryDataObj instanceof Boolean) {
			Boolean showHistoryData = (Boolean) showHistoryDataObj;
			result = showHistoryData.booleanValue();
		}
		
		return result;
	}

	public void setShowHistoryData(boolean showHistoryData, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean showHistoryDataBool = Boolean.valueOf(showHistoryData);
		session.setAttribute(MiraController.PARAM_NAME_SHOW_HISTORY_DATA, showHistoryDataBool);
	}
	
	public boolean getShowHistoryData(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object showHistoryDataObj = session.getAttribute(MiraController.PARAM_NAME_SHOW_HISTORY_DATA);
		boolean result = false;
		if (showHistoryDataObj != null && showHistoryDataObj instanceof Boolean) {
			Boolean showHistoryData = (Boolean) showHistoryDataObj;
			result = showHistoryData.booleanValue();
		}
		
		return result;
	}

}
