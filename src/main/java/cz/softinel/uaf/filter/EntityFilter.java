package cz.softinel.uaf.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is implementation of filter, used for filtering data on front-end.
 * 
 * @version $Revision: 1.3 $ $Date: 2007-01-29 11:39:05 $
 * @author Petr Sígl
 */
public abstract class EntityFilter implements Filter {

	private Map<String, String> fieldsMap = null;
	private List<String> fieldsList = null;

	/**
	 * Public constructor of filter. Need obtain list of fields for filter.
	 * 
	 * @param fields
	 */
	public EntityFilter(List<String> fields) {
		this.fieldsMap = new HashMap<String, String>();
		this.fieldsList = fields;
		prepareMap(this.fieldsList);
	}

	/**
	 * @see cz.softinel.uaf.filter.Filter#getAllFields()
	 */
	public List<String> getAllFields() {
		return Collections.unmodifiableList(fieldsList);
	}

	/**
	 * @see cz.softinel.uaf.filter.Filter#getFieldValue(java.lang.String)
	 */
	public String getFieldValue(String key) {
		return fieldsMap.get(key);
	}

	/**
	 * @see cz.softinel.uaf.filter.Filter#setFieldValue(java.lang.String,
	 *      java.lang.String)
	 */
	public void setFieldValue(String key, String value) {
		fieldsMap.put(key, value);
	}

	/**
	 * Prepare map according to list of fields. Map is better for putting and
	 * getting filter fields.
	 * 
	 * @param fields
	 */
	private void prepareMap(List<String> fields) {
		fieldsMap = new HashMap<String, String>();
		for (String key : fields) {
			fieldsMap.put(key, null);
		}
	}
}
