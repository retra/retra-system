package cz.softinel.retra.worklog.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import cz.softinel.retra.worklog.WorklogHelper;
import cz.softinel.uaf.spring.web.controller.AbstractCookieHelper;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * Worklog cookie helper.
 *
 * @version $Revision: 1.3 $ $Date: 2007-01-29 07:11:43 $
 * @author Petr Sígl
 */
public class WorklogCookieHelper extends AbstractCookieHelper {

	private static final String COOKIE_NAME_WORKLOG_DATE = "retra.worklogForm.date";
	private static final String COOKIE_NAME_WORKLOG_PROJECT = "retra.worklogForm.project";
	private static final String COOKIE_NAME_WORKLOG_COMPONENT = "retra.worklogForm.component";
	private static final String COOKIE_NAME_WORKLOG_ACTIVITY = "retra.worklogForm.activity";
	private static final String COOKIE_NAME_WORKLOG_FROM = "retra.worklogForm.from";
	private static final String COOKIE_NAME_WORKLOG_INVOICE = "retra.worklogForm.invoice";
	
	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#addToCookies(java.lang.Object, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public void addToCookies(Object commandForm, RequestContext requestContext) {
		Map helpParameters = new HashMap();
		helpParameters.put(WorklogHelper.HELP_COOKIE_PARAM_ADD_ALSO_FROM, Boolean.FALSE);
		addToCookies(commandForm, requestContext, helpParameters);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#addToCookies(java.lang.Object, javax.servlet.http.HttpServletResponse, java.util.Map)
	 */
	public void addToCookies(Object commandForm, RequestContext requestContext, Map helpParameters) {
		WorklogForm worklog = (WorklogForm) commandForm;
		boolean addAlsoFrom = false;
		if (helpParameters != null){
			Boolean helpParam = (Boolean)helpParameters.get(WorklogHelper.HELP_COOKIE_PARAM_ADD_ALSO_FROM);
			if (helpParam != null) {
				addAlsoFrom = helpParam.booleanValue();
			}
		}
		createAndAddCookie(COOKIE_NAME_WORKLOG_DATE, worklog.getDate(), requestContext);
		createAndAddCookie(COOKIE_NAME_WORKLOG_PROJECT, worklog.getProject(), requestContext);
		createAndAddCookie(COOKIE_NAME_WORKLOG_COMPONENT, worklog.getComponent(), requestContext);
		createAndAddCookie(COOKIE_NAME_WORKLOG_ACTIVITY, worklog.getActivity(), requestContext);
		createAndAddCookie(COOKIE_NAME_WORKLOG_INVOICE, worklog.getInvoice(), requestContext);
		if (addAlsoFrom) {
			//from is for next worklog item to
			createAndAddCookie(COOKIE_NAME_WORKLOG_FROM, worklog.getWorkTo(), requestContext);
		} else {
			createAndAddCookie(COOKIE_NAME_WORKLOG_FROM, "", requestContext);
		}
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#importFromCookies(java.lang.Object, javax.servlet.http.HttpServletRequest)
	 */
	public void importFromCookies(Object commandForm, RequestContext requestContext) {
		importFromCookies(commandForm, requestContext, null);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#importFromCookies(java.lang.Object, javax.servlet.http.HttpServletRequest, java.util.Map)
	 */
	public void importFromCookies(Object commandForm, RequestContext requestContext, Map helpParameters) {
		WorklogForm worklog = (WorklogForm) commandForm;
		Cookie[] cookies = requestContext.getCookies();
		if (cookies != null){
			for (Cookie cookie: cookies){
				String name = cookie.getName();
				String value = cookie.getValue();
				if (COOKIE_NAME_WORKLOG_DATE.equals(name)){
					worklog.setDate(value);
					continue;
				}
				if (COOKIE_NAME_WORKLOG_PROJECT.equals(name)){
					worklog.setProject(value);
					continue;
				}
				if (COOKIE_NAME_WORKLOG_COMPONENT.equals(name)){
					worklog.setComponent(value);
					continue;
				}
				if (COOKIE_NAME_WORKLOG_ACTIVITY.equals(name)){
					worklog.setActivity(value);
					continue;
				}
				if (COOKIE_NAME_WORKLOG_FROM.equals(name)){
					worklog.setWorkFrom(value);
					continue;
				}
				if (COOKIE_NAME_WORKLOG_INVOICE.equals(name)){
					worklog.setInvoice(value);
					continue;
				}
			}
		}	
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#cleanCookies(javax.servlet.http.HttpServletResponse)
	 */
	public void cleanCookies(RequestContext requestContext) {
		cleanCookies(requestContext, null);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#cleanCookies(javax.servlet.http.HttpServletResponse, java.util.Map)
	 */
	public void cleanCookies(RequestContext requestContext, Map helpParameters) {
		createAndAddCookie(COOKIE_NAME_WORKLOG_FROM, "", requestContext);
	}
}
