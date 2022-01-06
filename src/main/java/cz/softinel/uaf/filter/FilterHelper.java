package cz.softinel.uaf.filter;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.IntegerConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.core.utils.helper.DateHelper;
import cz.softinel.uaf.spring.web.controller.Context;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * This is Helper class for filters. It helps to get and set fields value. Helps
 * to put and get fields to request etc.
 * 
 * @version $Revision: 1.5 $ $Date: 2007-01-29 11:39:04 $
 * @author Petr Sígl
 */
public class FilterHelper {

	public static final String CLEAN_FILTER_PARAMETER = "cleanFilter";

	/**
	 * Clean or fill filter according to request.
	 * 
	 * @param filter
	 * @param request
	 */
	public static void cleanOrFillFilter(Filter filter, HttpServletRequest request) {
		if (cleanFilter(request)) {
			FilterHelper.cleanSavedFilter(filter, request);
		} else {
			FilterHelper.fillFilter(filter, request);
		}
	}

	/**
	 * Clean or fill filter according to requestContext.
	 * 
	 * @param filter
	 * @param requestContext
	 */
	public static void cleanOrFillFilter(Filter filter, RequestContext requestContext) {
		if (cleanFilter(requestContext)) {
			FilterHelper.cleanSavedFilter(filter, requestContext);
		} else {
			FilterHelper.fillFilter(filter, requestContext);
		}
	}

	/**
	 * This mehtod fills given filter from request (pointed by requestContext).
	 * 
	 * @param requestContext
	 * @param filter
	 */
	public static void fillFilter(Filter filter, RequestContext requestContext) {
		if (filter != null && requestContext != null && requestContext.getSessionContext() != null) {
			Context sessionContext = requestContext.getSessionContext();
			for (String key : filter.getAllFields()) {
				String value = requestContext.getParameter(key);
				if (value == null) {
					value = (String) sessionContext.getAttribute(key);
				}
				filter.setFieldValue(key, value);
			}
		}
	}

	/**
	 * This mehtod fills given filter from request.
	 * 
	 * @param request
	 * @param filter
	 */
	public static void fillFilter(Filter filter, HttpServletRequest request) {
		if (filter != null && request != null && request.getSession() != null) {
			HttpSession session = request.getSession();
			for (String key : filter.getAllFields()) {
				String value = request.getParameter(key);
				if (value == null) {
					value = (String) session.getAttribute(key);
				}
				filter.setFieldValue(key, value);
			}
		}
	}

	/**
	 * This method put all fields from filter to session (pointed by
	 * requestContext).
	 * 
	 * @param filter
	 * @param requestContext
	 */
	public static void saveFilter(Filter filter, RequestContext requestContext) {
		if (filter != null && requestContext != null && requestContext.getSessionContext() != null) {
			Context sessionContext = requestContext.getSessionContext();
			for (String key : filter.getAllFields()) {
				String value = filter.getFieldValue(key);
				sessionContext.setAttribute(key, value);
			}
		}
	}

	/**
	 * This method put all fields from filter to session.
	 * 
	 * @param filter
	 * @param request
	 */
	public static void saveFilter(Filter filter, HttpServletRequest request) {
		if (filter != null && request != null && request.getSession() != null) {
			HttpSession session = request.getSession();
			for (String key : filter.getAllFields()) {
				String value = filter.getFieldValue(key);
				session.setAttribute(key, value);
			}
		}
	}

	/**
	 * This method clean all filter parameters
	 * 
	 * @param filter
	 * @param requestContext
	 */
	public static void cleanSavedFilter(Filter filter, RequestContext requestContext) {
		if (filter != null && requestContext != null && requestContext.getSessionContext() != null) {
			Context sessionContext = requestContext.getSessionContext();
			for (String key : filter.getAllFields()) {
				sessionContext.setAttribute(key, null);
				filter.setFieldValue(key, null);
			}
		}
	}

	/**
	 * This method clean all filter parameters
	 * 
	 * @param filter
	 * @param request
	 */
	public static void cleanSavedFilter(Filter filter, HttpServletRequest request) {
		if (filter != null && request != null && request.getSession() != null) {
			HttpSession session = request.getSession();
			for (String key : filter.getAllFields()) {
				session.setAttribute(key, null);
				filter.setFieldValue(key, null);
			}
		}
	}

	/**
	 * Returns field from filter as string.
	 * 
	 * @param key
	 * @param filter
	 * @return
	 */
	public static String getFieldAsString(String key, Filter filter) {
		String valueStr = filter.getFieldValue(key);
		String result = null;

		if (valueStr == null || valueStr.equals("null")) {
			return result;
		}
		result = valueStr;

		return result;
	}

	/**
	 * Returns field from filter as long.
	 * 
	 * @param key
	 * @param filter
	 * @return
	 */
	public static Long getFieldAsLong(String key, Filter filter) {
		String valueStr = filter.getFieldValue(key);
		Long result = null;
		try {
			result = LongConvertor.convertToLongFromString(valueStr);
		} catch (ConvertException e) {
			// TODO: add warning message
			result = null;
		}

		return result;
	}

	/**
	 * Returns field from filter as long.
	 * 
	 * @param key
	 * @param filter
	 * @return
	 */
	public static Integer getFieldAsInteger(String key, Filter filter) {
		String valueStr = filter.getFieldValue(key);
		Integer result = null;
		try {
			result = IntegerConvertor.convertToIntegerFromString(valueStr);
		} catch (ConvertException e) {
			// TODO: add warning message
			result = null;
		}

		return result;
	}

	/**
	 * Returns field from filter as date.
	 * 
	 * @param key
	 * @param filter
	 * @return
	 */
	public static Date getFieldAsDate(String key, Filter filter) {
		return getFieldAsDate(key, filter, false);
	}

	/**
	 * Returns field from filter as date.
	 * 
	 * @param key
	 * @param filter
	 * @param endOfDay this parameter means, that if hours and minutes are not
	 *                 defined define it as end of day.
	 * @return
	 */
	public static Date getFieldAsDate(String key, Filter filter, boolean endOfDay) {
		String valueStr = filter.getFieldValue(key);
		Date result = null;
		// continuosly try to get date, one by one for patterns
		result = DateConvertor.getDateFromFullDateString(valueStr);

		if (result == null) {
			result = DateConvertor.getDateFromDateHourMinuteSecondString(valueStr);
		}
		if (result == null) {
			result = DateConvertor.getDateFromDateHourMinuteString(valueStr);
		}
		if (result == null) {
			// last chance to get date
			try {
				result = DateConvertor.convertToDateFromDateString(valueStr);
				if (result != null && endOfDay) {
					result = DateHelper.getEndOfDay(result);
				}
			} catch (ConvertException e) {
				// TODO: add warning message
				result = null;
			}
		}

		return result;
	}

	/**
	 * Returns field from filter as date - first day of year and month.
	 * 
	 * @param yearKey
	 * @param monthKey
	 * @param filter
	 * @return
	 */
	public static Date getFieldAsFirstDayDate(String yearKey, String monthKey, Filter filter) {
		String yearStr = filter.getFieldValue(yearKey);
		String monthStr = filter.getFieldValue(monthKey);

		Date result = null;
		try {
			Integer year = IntegerConvertor.convertToIntegerFromString(yearStr);
			Integer month = IntegerConvertor.convertToIntegerFromString(monthStr);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, 1, 0, 0, 0);
			result = DateHelper.getStartOfMonth(calendar.getTime());
		} catch (ConvertException e) {
			// TODO: add warning message
			result = null;
		}

		return result;
	}

	/**
	 * Returns field from filter as date - last day of year and month.
	 * 
	 * @param year
	 * @param month
	 * @param filter
	 * @return
	 */
	public static Date getFieldAsLastDayDate(String yearKey, String monthKey, Filter filter) {
		String yearStr = filter.getFieldValue(yearKey);
		String monthStr = filter.getFieldValue(monthKey);

		Date result = null;
		try {
			Integer year = IntegerConvertor.convertToIntegerFromString(yearStr);
			Integer month = IntegerConvertor.convertToIntegerFromString(monthStr);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, 1, 0, 0, 0);
			result = DateHelper.getEndOfMonth(calendar.getTime());
		} catch (ConvertException e) {
			// TODO: add warning message
			result = null;
		}

		return result;
	}

	/**
	 * Returns field from filter as boolean.
	 * 
	 * @param key
	 * @param filter
	 * @retur
	 */
	public static Boolean getFieldAsBoolean(String key, Filter filter) {
		String valueStr = filter.getFieldValue(key);
		Boolean result = null;

		if (valueStr == null || valueStr.equals("null")) {
			return result;
		}

		try {
			result = Boolean.valueOf(valueStr);
		} catch (Exception e) {
			// TODO: add warning message
			result = null;
		}

		return result;
	}

	/**
	 * Set field filter value
	 * 
	 * @param key
	 * @param value  long value
	 * @param filter
	 */
	public static void setField(String key, String value, Filter filter) {
		filter.setFieldValue(key, value);
	}

	/**
	 * Set field filter value
	 * 
	 * @param key
	 * @param value  long value
	 * @param filter
	 */
	public static void setField(String key, Long value, Filter filter) {
		String valueStr = LongConvertor.convertToStringFromLong(value);
		filter.setFieldValue(key, valueStr);
	}

	/**
	 * Set field filter value
	 * 
	 * @param key
	 * @param value  long value
	 * @param filter
	 */
	public static void setField(String key, Integer value, Filter filter) {
		String valueStr = IntegerConvertor.convertToStringFromInteger(value);
		filter.setFieldValue(key, valueStr);
	}

	/**
	 * Set field filter value
	 * 
	 * @param key
	 * @param value  date value
	 * @param filter
	 */
	public static void setField(String key, Date value, Filter filter) {
		setField(key, value, false, filter);
	}

	/**
	 * Set field filter value
	 * 
	 * @param key
	 * @param value    date value
	 * @param justDate if put just date (without time)
	 * @param filter
	 */
	public static void setField(String key, Date value, boolean justDate, Filter filter) {
		String valueStr = null;
		if (justDate) {
			valueStr = DateConvertor.convertToDateStringFromDate(value);
		} else {
			valueStr = DateConvertor.convertToFullDateStringFromDate(value);
		}
		filter.setFieldValue(key, valueStr);
	}

	/**
	 * Set field filter value
	 * 
	 * @param key
	 * @param value  long value
	 * @param filter
	 */
	public static void setField(String key, Boolean value, Filter filter) {
		String valueStr = null;
		if (value != null) {
			valueStr = value.toString();
		}
		filter.setFieldValue(key, valueStr);
	}

	/**
	 * Help method which return if filter should be cleared or not.
	 * 
	 * @param request
	 * @return
	 */
	private static boolean cleanFilter(HttpServletRequest request) {
		boolean result = false;
		String cleanFilterStr = (String) request.getAttribute(FilterHelper.CLEAN_FILTER_PARAMETER);
		if (!StringUtils.hasText(cleanFilterStr)) {
			cleanFilterStr = request.getParameter(FilterHelper.CLEAN_FILTER_PARAMETER);
		}
		if (StringUtils.hasText(cleanFilterStr)) {
			result = Boolean.valueOf(cleanFilterStr);
		}

		return result;
	}

	/**
	 * Help method which return if filter should be cleared or not.
	 * 
	 * @param requestContext
	 * @return
	 */
	private static boolean cleanFilter(RequestContext requestContext) {
		boolean result = false;
		String cleanFilterStr = (String) requestContext.getAttribute(FilterHelper.CLEAN_FILTER_PARAMETER);
		if (!StringUtils.hasText(cleanFilterStr)) {
			cleanFilterStr = requestContext.getParameter(FilterHelper.CLEAN_FILTER_PARAMETER);
		}
		if (StringUtils.hasText(cleanFilterStr)) {
			result = Boolean.valueOf(cleanFilterStr);
		}

		return result;
	}
}