package cz.softinel.sis.security.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.softinel.sis.user.User;
import cz.softinel.uaf.spring.web.controller.AbstractCookieHelper;
import cz.softinel.uaf.util.web.CookieHelper;

public class SecurityWebFilter implements Filter {

	private static Log logger = LogFactory.getLog(SecurityWebFilter.class);

	// FIXME radek: This is really hack (how configure servlet/filter by spring)
	public static WebSecurityLogic securityLogic;
	
	public void init(FilterConfig config) throws ServletException {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req =(HttpServletRequest) request;
		try {
			boolean process = false;
			logger.info("Processing secured request: " + req.getRequestURL());
			securityLogic.initContext(req);
			// FIXME radek: This is FAKE ... very FAKE implementation ;-)
			if (securityLogic.isUserLoggedIn()) {
				process = true;
			} else {
				if (securityLogic.isUnsecuredPage(req.getRequestURI())) {
					process = true;
				} else {
					String permanentPassword = CookieHelper.getCookie(AbstractCookieHelper.RETRA_PERMANENT_COOKIE_NAME, req);
					if (permanentPassword != null) {
						User user = securityLogic.login(permanentPassword);
						if (user != null) {
							process = true;
						}
					}
				}
			}
			// TODO radek: Check, if login is required
			// TODO radek: Check, if is URL permitted ???
			if (process) {
				chain.doFilter(request, response);
			} else {
				// TODO radek: Keep original URL for redirect after successfully login
				String originalUrl = req.getRequestURL().toString();
				String separator = "?";
				for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
					String name = e.nextElement();
					originalUrl += separator + name + "=" + request.getParameter(name);
					separator = "&";
				}
				request.setAttribute("originalUrl", URLEncoder.encode(originalUrl, "UTF-8"));
				request.getRequestDispatcher("Login.do?fkprm=true").forward(request, response);
			}
			// TODO radek: Redirect to login screen
			// TODO radek: Keep original URL for deep linking
		} finally {
			securityLogic.doneContext(req);
		}
	}

}
