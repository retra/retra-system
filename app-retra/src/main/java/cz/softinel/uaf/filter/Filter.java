package cz.softinel.uaf.filter;

import java.util.List;

/**
 * Interface for filter. It contains all needed functions for filtering data.
 * 
 * @version $Revision: 1.2 $ $Date: 2007-01-26 08:38:25 $
 * @author Petr Sígl
 */
public interface Filter {

	/**
	 * Returns list of all field names.
	 * 
	 * @return
	 */
	public List<String> getAllFields();

	/**
	 * Returns value of field defined by key.
	 * 
	 * @param key
	 * @return
	 */
	public String getFieldValue(String key);

	/**
	 * Set value to field defined by key.
	 * 
	 * @param key
	 * @param value
	 */
	public void setFieldValue(String key, String value);
}
