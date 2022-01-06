package cz.softinel.retra.spring.web;

import javax.servlet.http.HttpServletRequest;

import cz.softinel.retra.security.blo.MiraSecurityLogic;
import cz.softinel.uaf.spring.web.controller.CommonFormController;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public abstract class FormController extends CommonFormController implements MiraController {

	MiraController miraController = new MiraControllerImpl();

	public String getLoginPageAction() {
		return miraController.getLoginPageAction();
	}

	public MiraSecurityLogic getSecurityLogic() {
		return miraController.getSecurityLogic();
	}

	public boolean isRequireLogin() {
		return miraController.isRequireLogin();
	}

	public void setLoginPageAction(String loginPageAction) {
		miraController.setLoginPageAction(loginPageAction);
	}

	public void setRequireLogin(boolean requireLogin) {
		miraController.setRequireLogin(requireLogin);
	}

	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		miraController.setSecurityLogic(securityLogic);
	}

	public void setShowHistoryData(boolean showHistoryData, RequestContext requestContext) {
		miraController.setShowHistoryData(showHistoryData, requestContext);
	}

	public boolean getShowHistoryData(RequestContext requestContext) {
		return miraController.getShowHistoryData(requestContext);
	}

	public void setShowHistoryData(boolean showHistoryData, HttpServletRequest request) {
		miraController.setShowHistoryData(showHistoryData, request);
	}

	public boolean getShowHistoryData(HttpServletRequest request) {
		return miraController.getShowHistoryData(request);
	}
}
