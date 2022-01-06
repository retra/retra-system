package cz.softinel.sis.core;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter for prevent cache on browser.
 * 
 * @author petr
 *
 */
public class NoCacheFilter implements Filter {

	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Expires", "Tue, 01 Jan 1980 1:00:00 GMT");
		resp.setHeader("Last-Modified", new Date().toString());
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		resp.setHeader("Pragma", "no-cache");

		chain.doFilter(request, response);
	}

}
