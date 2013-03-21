package cz.softinel.retra.spring.web;

import javax.servlet.http.HttpServletRequest;

import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public interface MiraController {

	public final static String PARAM_NAME_SHOW_HISTORY_DATA = "showHistoryData";
	public final static String PARAM_NAME_AFTER_SET_ACTION = "afterSetAction";

	public String getLoginPageAction();
	public void setLoginPageAction(String loginPageAction);
	
	public boolean isRequireLogin();
	public void setRequireLogin(boolean requireLogin);
	
	public MiraSecurityLogic getSecurityLogic();
	public void setSecurityLogic(MiraSecurityLogic securityLogic);

	public void setShowHistoryData(boolean showHistoryData, RequestContext requestContext);
	public boolean getShowHistoryData(RequestContext requestContext);

	public void setShowHistoryData(boolean showHistoryData, HttpServletRequest request);
	public boolean getShowHistoryData(HttpServletRequest request);
}
