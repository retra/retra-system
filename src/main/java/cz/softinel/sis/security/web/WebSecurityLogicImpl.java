package cz.softinel.sis.security.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cz.softinel.sis.security.SecurityContext;
import cz.softinel.sis.security.blo.SecurityLogicImpl;

// TODO radek: Check memory leaks in storage!!!
// TODO radek: Use ThreadLocal instead storage map.
// TODO radek: Use ACEGI instead own implementation ???
public abstract class WebSecurityLogicImpl extends SecurityLogicImpl implements WebSecurityLogic {

	private static Map<Thread,SecurityContext> storage = new HashMap<Thread,SecurityContext>();
	
	private static Map<String, String> unsecuredPages = new HashMap<String, String>();
	
	private static final String SECURITY_CONTEXT_KEY = "securityContext";

	public void initContext(HttpServletRequest request) {
		// TODO radek: Check memory leaks!!!
		HttpSession session = request.getSession();
		SecurityContext securityContext = (SecurityContext) session.getAttribute(SECURITY_CONTEXT_KEY);
		if (securityContext == null) {
			// TODO radek: Configure security context implementation by spring
			securityContext = newSecurityContext();
			session.setAttribute(SECURITY_CONTEXT_KEY, securityContext);
			System.out.println("Creating new securityContext");
		} else {
			System.out.println("Found security context "+securityContext.getClass());
		}
		SecurityContext originalSecurityContext = null;
		synchronized (storage) {
			originalSecurityContext = storage.put(getSecurityContextIdentificator(), securityContext);
		}
		if (originalSecurityContext != null) {
			// TODO radek: Log Warning: security context already initialized (was not finalized)
		}
		initUnsecuredPages(request);
	}
	
	public boolean isUnsecuredPage(String page) {
		if(page == null) {
			return false;
		}
		return unsecuredPages.containsKey(page);
	}

	private void initUnsecuredPages(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		unsecuredPages.put(contextPath + "/index.jsp", "");
		unsecuredPages.put(contextPath + "/Login.do", "");
		unsecuredPages.put(contextPath + "/LoginAction.do", "");
		unsecuredPages.put(contextPath + "/Logout.do", "");
	}

	public void doneContext(HttpServletRequest request) {
		// TODO radek: Check memory leaks!!!
		SecurityContext originalSecurityContext = null;
		synchronized (storage) {
			originalSecurityContext = storage.remove(getSecurityContextIdentificator());
		}
		if (originalSecurityContext == null) {
			// TODO radek: Log Warning: Security context was not initialized (or already finalized)
		}
	}
	
	public SecurityContext getSecurityContext() {
		SecurityContext securityContext = null;
		synchronized (storage) {
			securityContext = storage.get(getSecurityContextIdentificator());
		}
		if (securityContext == null) {
			throw new RuntimeException("Security context was not initialized.");
		}
		return securityContext;
	}

	private Thread getSecurityContextIdentificator() {
		return Thread.currentThread();
	}
	
	// TODO radek: Organize ...
	public abstract SecurityContext newSecurityContext();

}
