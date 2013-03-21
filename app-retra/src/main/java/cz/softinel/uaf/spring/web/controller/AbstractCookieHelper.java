package cz.softinel.uaf.spring.web.controller;

import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * This is abstract parent of all cookies helpers. You should re-implement all
 * methods.
 *
 * @version $Revision: 1.2 $ $Date: 2007-02-17 12:14:00 $
 * @author Petr Sígl
 */
public abstract class AbstractCookieHelper {
	
	public static final int COOKIE_MAX_AGE = Integer.MAX_VALUE;
	public static final String COOKIE_COMMENT = "Retra cookie";
	public static final String RETRA_PERMANENT_COOKIE_NAME = "RETRA-PermanentPassword";
	
	/**
	 * This method is used for common adding something to cookies.
	 * 
	 * @param commandForm form or entity from which add to cookies
	 * @param requestContext for setting cookie
	 */
	public void addToCookies(Object commandForm, RequestContext requestContext){
		addToCookies(commandForm, requestContext, null);
	}

	/**
	 * This method is used for adding something to cookies and if you need some
	 * help parameters than use map for it.
	 * 
	 * @param commandForm
	 * @param requestContext
	 * @param helpParameters
	 */
	public void addToCookies(Object commandForm, RequestContext requestContext, Map helpParameters){
	}

	/**
	 * Import data from cookies into object
	 * 
	 * @param commandForm
	 * @param requestContext
	 */
	public void importFromCookies(Object commandForm, RequestContext requestContext){
		importFromCookies(commandForm, requestContext, null);
	}

	/**
	 * Import data from cookies to object and when you need some help parameters use map.
	 * 
	 * @param commandForm
	 * @param requestContext
	 * @param helpParameters
	 */
	public void importFromCookies(Object commandForm, RequestContext requestContext, Map helpParameters){
	}
	
	/**
	 * Sometimes is needed to clean cookies - so implement this method.
	 * 
	 * @param requestContext
	 */
	public void cleanCookies(RequestContext requestContext){
		cleanCookies(requestContext, null);
	}

	/**
	 * Implement if needed to clean cookies and for cleaning is needed some
	 * help parameters - use map.
	 * 
	 * @param requestContext
	 * @param helpParameters
	 */
	public void cleanCookies(RequestContext requestContext, Map helpParameters){
	}
	
	/**
	 * This method creates default cookie
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public Cookie createCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(COOKIE_MAX_AGE);
		cookie.setComment(COOKIE_COMMENT);
		return cookie;
	}
	
	/**
	 * This method creates cookie and set it to response.
	 * 
	 * @param name
	 * @param value
	 * @param response
	 * @return
	 */
	public Cookie createAndAddCookie(String name, String value, RequestContext requestContext) {
		Cookie cookie = createCookie(name, value);
		requestContext.addCookie(cookie);
		return cookie;
	}
}
