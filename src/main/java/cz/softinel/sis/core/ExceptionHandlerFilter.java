package cz.softinel.sis.core;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.util.NestedServletException;

import cz.softinel.sis.security.NoPermissionException;

/**
 * Filter for prevent cache on browser.
 * 
 * @author petr
 *
 */
public class ExceptionHandlerFilter implements Filter {

	private static Random rnd = new Random();
	private static Logger logger = Logger.getLogger(ExceptionHandlerFilter.class);
	private static DecimalFormat df = new DecimalFormat("0000000000000000000");
	
	
	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try	{
			chain.doFilter(request, response);
		} catch (NestedServletException e) {
			if (e.getRootCause() instanceof NoPermissionException) {
				logger.error("No permission for user.");
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				String url = "NoPermission.do";
				httpResponse.sendRedirect(url);
			} else {
				//same as in catch (Throwable e)
				Long id = Math.abs(rnd.nextLong());
				String errorCode = "ERRID-" + df.format(id);
				logger.error("Error - " + errorCode, e);
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				String url = "Error.do?errId=" + errorCode;
				httpResponse.sendRedirect(url);
			}
		}
		catch (Throwable e) {
			Long id = Math.abs(rnd.nextLong());
			String errorCode = "ERRID-" + df.format(id);
			logger.error("Error - " + errorCode, e);
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			String url = "Error.do?errId=" + errorCode;
			httpResponse.sendRedirect(url);
		}
	}
	
}
