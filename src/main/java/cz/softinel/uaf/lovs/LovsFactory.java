package cz.softinel.uaf.lovs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.Resource;

import cz.softinel.uaf.messages.Message;

/**
 * This class is factory for using list of values.
 *
 * @version $Revision: 1.6 $ $Date: 2007-04-05 09:16:01 $
 * @author Petr Sígl
 */
public final class LovsFactory extends ApplicationObjectSupport {
	// logger
	private static Logger logger = LoggerFactory.getLogger(LovsFactory.class);

	// private lov files
	private List<String> lovFiles;

	// singleton of this class
	private static LovsFactory lovsFactory = new LovsFactory();

	// private variables
	private Lovs lovs;
	private Map<String, Lov> lovsInMap;

	/**
	 * Private constructor for lovsFactory
	 */
	private LovsFactory() {
		lovs = null;
		lovsInMap = null;
		lovsFactory = this;
	}

	/**
	 * @return Returns the lovFiles.
	 */
	public List<String> getLovFiles() {
		return lovFiles;
	}

	/**
	 * @param lovFiles The lovFiles to set.
	 */
	public void setLovFiles(List<String> lovFiles) {
		this.lovFiles = lovFiles;
	}

	/**
	 * Returns instance of lovs factory
	 * 
	 * @return lovs factory
	 */
	public static LovsFactory getInstance() {
		return lovsFactory;
	}

	/**
	 * Return list of values according to code.
	 *
	 * @param code
	 * @param context
	 * @return
	 */
	public Lov getLov(String code, ApplicationContext applicationContext) {
		if (lovs == null || lovsInMap == null) {
			lovs = new Lovs();
			loadListOfValues();
		}

		Lov lov = lovsInMap.get(code);
		if (lov == null || lov.getFields() == null) {
			throw new RuntimeException("Missing LOV for code: " + code);
		}
		// TODO: Check ... it is good solution?
		// FIXME: siglp - I think this is not good way, because of calling everytime,
		// when lov needed. I think
		// better is to have only keys to resource bundles and where I need label (jsp,
		// tag etc.) I can take it from resource bundle
		for (LovField lovField : lov.getFields()) {
			Message message = new Message(lovField.getKey());
			String label = applicationContext.getMessage(message.getValueOrKey(), message.getParameters(),
					message.getDefaultValue(), message.getLocale());
			lovField.setLabel(label);
		}

		return lov;

	}

	/**
	 * Load all list of values.
	 */
	private void loadListOfValues() {
		for (String fileName : lovFiles) {
			Resource resource = getApplicationContext().getResource(fileName);
			// try to load from xml
			try {
				JAXBContext jc = JAXBContext.newInstance("cz.softinel.uaf.lovs");
				Unmarshaller u = jc.createUnmarshaller();
				Lovs loadedLovs = (Lovs) u.unmarshal(resource.getFile());
				lovs.addLovs(loadedLovs.getLovs());
			} catch (JAXBException e) {
				logger.warn("Couldn't load list of values file: " + fileName + ". ", e);
			} catch (IOException e) {
				logger.warn("Couldn't load list of values file: " + fileName + ". ", e);
			}
		}
		lovsInMap = createLovMap(lovs);
	}

	/**
	 * Create map of list of values, where key is lov-code and value is lov.
	 * 
	 * @param lovs list from which creates
	 * @return created map
	 */
	private Map<String, Lov> createLovMap(Lovs lovs) {
		Map<String, Lov> map = null;
		if (lovs != null) {
			map = new HashMap<String, Lov>();
			for (Lov lov : lovs.getLovs()) {
				map.put(lov.getCode(), lov);
			}
		}
		return map;
	}
}
