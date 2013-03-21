package cz.softinel.sis.security.web;

import javax.servlet.http.HttpServletRequest;

import cz.softinel.sis.security.blo.SecurityLogic;

/**
 * 
 * @author borisha
 *
 */
public interface WebSecurityLogic extends SecurityLogic {

	public void initContext(HttpServletRequest request);

	public void doneContext(HttpServletRequest request);

	public boolean isUnsecuredPage(String page);

}
