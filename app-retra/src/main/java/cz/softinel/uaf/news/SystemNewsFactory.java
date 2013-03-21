package cz.softinel.uaf.news;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.Resource;

/**
 * This class is factory for using list of values.
 * 
 * @version $Revision: 1.2 $ $Date: 2007-02-17 12:14:00 $
 * @author Petr Sígl
 */
//FIXME: refactor this class - package, it is HOTFIXED
public final class SystemNewsFactory extends ApplicationObjectSupport {
	// logger
	private static Log logger = LogFactory.getLog(SystemNewsFactory.class);

	// private systemNews file
	private String systemNewsFile;

	// private variables
	private SystemNews systemNews;

	/**
	 * Private constructor for lovsFactory
	 */
	private SystemNewsFactory() {
		systemNews = null;
	}

	/**
	 * @return the systemNewsFile
	 */
	public String getSystemNewsFile() {
		return systemNewsFile;
	}

	/**
	 * @param systemNewsFile the systemNewsFile to set
	 */
	public void setSystemNewsFile(String systemNewsFile) {
		this.systemNewsFile = systemNewsFile;
	}

	public List getSystemNews(){
		if (systemNews == null) {
			loadSystemNews();
		}
		
		return Collections.unmodifiableList(systemNews.getNews());
	}
	
	/**
	 * Load all systemNews
	 */
	private void loadSystemNews() {
		Resource resource = getApplicationContext().getResource(systemNewsFile);
		// try to load from xml
		try {
			JAXBContext jc = JAXBContext.newInstance("cz.softinel.uaf.news");
			Unmarshaller u = jc.createUnmarshaller();
			systemNews = (SystemNews) u.unmarshal(resource.getFile());
		} catch (JAXBException e) {
			logger.warn("Couldn't load systemNews file: " + systemNewsFile + ". ", e);
		} catch (IOException e) {
			logger.warn("Couldn't load systemNews file: " + systemNewsFile + ". ", e);
		}
	}
}
