package cz.softinel.retra.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Fix for Spring 2.5.6 - java 8 is not supported => 1.7.0_888.
 * 
 * @author petr
 *
 */
public class JavaVersionWorkaroundListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.setProperty("java.version", "1.7.0_888");
		final String configLocation = sce.getServletContext().getInitParameter("retra.config.location");
		if (configLocation != null) {
			System.setProperty("retra.config.location", configLocation);
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
