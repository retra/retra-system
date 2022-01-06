package cz.softinel.retra.core.utils.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cz.softinel.retra.core.utils.convertor.DateConvertor;

public class DateHelper {

	// FIXME radek: This is not thread-safe !!!
	private static final Calendar calendar = Calendar.getInstance();

	public static Date getStartOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}

	public static Date getEndOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		date = calendar.getTime();
		return date;
	}

	public static Date getStartOfMonth(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}

	public static Date getEndOfMonth(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		date = calendar.getTime();
		return date;
	}

	public static Date getDayBeforeDateStartOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	public static Date getWeekBeforeDateStartOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		date = calendar.getTime();
		return date;
	}

	public static Date getDayBeforeDateEndOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MILLISECOND, -1);
		date = calendar.getTime();
		return date;
	}

	public static Date getDayAfterDateStartOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		date = calendar.getTime();
		return date;
	}

	public static Date getDayAfterDateEndOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 2);
		calendar.add(Calendar.MILLISECOND, -1);
		date = calendar.getTime();
		return date;
	}

	public static Date getTodayDateEndOfDay(Date date) {
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		date = calendar.getTime();
		return date;
	}

	public static String getWeekCode(Date date) {
		// FIXME radek: Use correct locale - it is only hack for right week numbering
		return new SimpleDateFormat("yyyy/ww", Locale.GERMANY).format(date);
	}

	public static String getWeekLabel(Date date) {
		StringBuffer result = new StringBuffer();
		result.append(new SimpleDateFormat("yyyy/w").format(date));
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 2); // monday
		result.append(" (");
		result.append(DateConvertor.convertToDateStringFromDate(calendar.getTime()));
		result.append(" - ");
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		result.append(DateConvertor.convertToDateStringFromDate(calendar.getTime()));
		result.append(")");
		return result.toString();
	}

	public static String getCurrentWeek() {
		return getWeekCode(new Date());
	}

	public static Date getFirstDayOfWeek(String weekNumber) {
		String[] parts = weekNumber.split("/");
		int year = Integer.valueOf(parts[0]);
		int week = Integer.valueOf(parts[1]);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, 2); // The day-of-week is an integer value where 1 is Sunday, 2 is Monday,
												// ..., and 7 is Saturday
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getFirstDayOfNextWeek(String weekNumber) {
		String[] parts = weekNumber.split("/");
		int year = Integer.valueOf(parts[0]);
		int week = Integer.valueOf(parts[1]);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.DAY_OF_WEEK, 2); // The day-of-week is an integer value where 1 is Sunday, 2 is Monday,
												// ..., and 7 is Saturday
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		return calendar.getTime();
	}

	public static class Week {
		private final String code;
		private final String label;

		public String getCode() {
			return code;
		}

		public String getLabel() {
			return label;
		}

		public Week(String code, String label) {
			this.code = code;
			this.label = label;
		}
	}

	public static List<Week> getWeekList(int length) {
		LinkedList<Week> list = new LinkedList<Week>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		for (int i = 0; i < length; i++) {
			String weekCode = getWeekCode(cal.getTime());
			String weekLabel = getWeekLabel(cal.getTime());
			list.addFirst(new Week(weekCode, weekLabel));
			cal.add(Calendar.WEEK_OF_YEAR, -1);
		}
		return list;
	}

	public static Date add(Date date, int field, int amount) {
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}
}
