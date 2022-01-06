package cz.softinel.retra.schedule.web;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;

import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.uaf.spring.web.controller.AbstractCookieHelper;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * Schedule cookie helper.
 *
 * @version $Revision: 1.1 $ $Date: 2007-01-29 07:11:43 $
 * @author Petr Sígl
 */
public class ScheduleCookieHelper extends AbstractCookieHelper {

	private static final String COOKIE_NAME_SCHEDULE_DATE = "mira.scheduleForm.date";
	private static final String COOKIE_NAME_SCHEDULE_TYPE = "mira.scheduleForm.type";
	private static final String COOKIE_NAME_SCHEDULE_FROM = "mira.scheduleForm.from";
	private static final String COOKIE_NAME_SCHEDULE_TO = "mira.scheduleForm.to";

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#addToCookies(java.lang.Object,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	public void addToCookies(Object commandForm, RequestContext requestContext) {
		addToCookies(commandForm, requestContext, null);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#addToCookies(java.lang.Object,
	 *      javax.servlet.http.HttpServletResponse, java.util.Map)
	 */
	public void addToCookies(Object commandForm, RequestContext requestContext, Map helpParameters) {
		ScheduleForm schedule = (ScheduleForm) commandForm;
		// add one day
		String date = schedule.getDate();
		Date datePlus = DateConvertor.getDateFromDateString(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(datePlus);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		datePlus = calendar.getTime();
		String datePlusStr = DateConvertor.convertToDateStringFromDate(datePlus);
		createAndAddCookie(COOKIE_NAME_SCHEDULE_DATE, datePlusStr, requestContext);
		createAndAddCookie(COOKIE_NAME_SCHEDULE_TYPE, schedule.getType(), requestContext);
		createAndAddCookie(COOKIE_NAME_SCHEDULE_FROM, schedule.getWorkFrom(), requestContext);
		createAndAddCookie(COOKIE_NAME_SCHEDULE_TO, schedule.getWorkTo(), requestContext);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#importFromCookies(java.lang.Object,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	public void importFromCookies(Object commandForm, RequestContext requestContext) {
		importFromCookies(commandForm, requestContext, null);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.AbstractCookieHelper#importFromCookies(java.lang.Object,
	 *      javax.servlet.http.HttpServletRequest, java.util.Map)
	 */
	public void importFromCookies(Object commandForm, RequestContext requestContext, Map helpParameters) {
		ScheduleForm schedule = (ScheduleForm) commandForm;
		Cookie[] cookies = requestContext.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				if (COOKIE_NAME_SCHEDULE_DATE.equals(name)) {
					schedule.setDate(value);
					continue;
				}
				if (COOKIE_NAME_SCHEDULE_TYPE.equals(name)) {
					schedule.setType(value);
					continue;
				}
				if (COOKIE_NAME_SCHEDULE_FROM.equals(name)) {
					schedule.setWorkFrom(value);
					continue;
				}
				if (COOKIE_NAME_SCHEDULE_TO.equals(name)) {
					schedule.setWorkTo(value);
					continue;
				}
			}
		}
	}
}
