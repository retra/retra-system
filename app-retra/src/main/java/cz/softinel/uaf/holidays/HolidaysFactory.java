package cz.softinel.uaf.holidays;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.Resource;

/**
 * This class is factory for using public holidays.
 * 
 * @version $Revision: 1.3 $ $Date: 2007-04-05 09:16:01 $
 * @author Petr Sígl
 */
public final class HolidaysFactory extends ApplicationObjectSupport {
	// logger
	private static Log logger = LogFactory.getLog(HolidaysFactory.class);

	// pattern
	private final static String datePattern = "d.M.";
	private final static String validPattern = "d.M.yyyy";
	private DateFormat dateFormat = new SimpleDateFormat(datePattern);
	private DateFormat validFormat = new SimpleDateFormat(validPattern);
	
	// private holidays file
	private String holidaysFile;

	// singleton of this class
	private static HolidaysFactory holidaysFactory = new HolidaysFactory();

	// private variables
	private Holidays holidays;

	/**
	 * Private constructor for holidaysFactory
	 */
	private HolidaysFactory() {
		holidays = null;
		holidaysFactory = this;
	}

	/**
	 * @return the holidaysFile
	 */
	public String getHolidaysFile() {
		return holidaysFile;
	}

	/**
	 * @param holidaysFile the holidaysFile to set
	 */
	public void setHolidaysFile(String holidaysFile) {
		this.holidaysFile = holidaysFile;
	}

	/**
	 * Returns instance of holidays factory
	 * 
	 * @return holidays factory
	 */
	public static HolidaysFactory getInstance() {
		return holidaysFactory;
	}

	/**
	 * Return list of values according to code.
	 * 
	 * @param code
	 * @return
	 */
	public boolean isPublicHoliday(Date date) {
		if (date == null) {
			return false;
		}

		String dateStr = dateFormat.format(date);
		
		if (holidays == null) {
			loadHolidays();
		}

		for (Item item : holidays.getItems()) {
			for (Term term: item.getTerms()) {
				String findDate = term.getDate();
				if (findDate.equals(dateStr)) {
					try {
						Date validFrom = validFormat.parse(term.getValidFrom());
						Date validTo = validFormat.parse(term.getValidTo());
						if (date.after(validFrom) && date.before(validTo)) {
							return true;
						}
					}
					catch (ParseException e) {
						return false;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Load all holidays
	 */
	private void loadHolidays() {
		Resource resource = getApplicationContext().getResource(holidaysFile);
		// try to load from xml
		try {
			JAXBContext jc = JAXBContext.newInstance("cz.softinel.uaf.holidays");
			Unmarshaller u = jc.createUnmarshaller();
			holidays = (Holidays) u.unmarshal(resource.getFile());
		} catch (JAXBException e) {
			logger.warn("Couldn't load holidays file: " + holidaysFile + ". ", e);
		} catch (IOException e) {
			logger.warn("Couldn't load holidays file: " + holidaysFile + ". ", e);
		}
	}
}
