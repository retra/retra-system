package cz.softinel.retra.worklog.web;

/**
 * Thrown while parsing worklog import data.
 *
 * @version $Revision: 1.2 $ $Date: 2007-02-23 12:16:27 $
 * @author Pavel Mueller
 */
@SuppressWarnings("serial")
public class ImportParsingException extends RuntimeException {

	/**
	 * @param message
	 * @param cause
	 */
	public ImportParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ImportParsingException(String message) {
		super(message);
	}

}
