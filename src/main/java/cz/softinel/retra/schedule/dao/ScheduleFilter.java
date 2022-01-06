package cz.softinel.retra.schedule.dao;

import java.util.ArrayList;
import java.util.List;

import cz.softinel.uaf.filter.EntityFilter;

public class ScheduleFilter extends EntityFilter {

	public static final String SCHEDULE_FILTER_MONTH = "scheduleFilterMonth";
	public static final String SCHEDULE_FILTER_YEAR = "scheduleFilterYear";
	public static final String SCHEDULE_FILTER_EMPLOYEE = "scheduleFilterEmployee";
	public static final String SCHEDULE_FILTER_FROM = "scheduleFilterFrom";
	public static final String SCHEDULE_FILTER_TO = "scheduleFilterTo";
	public static final String SCHEDULE_FILTER_TYPE = "scheduleFilterType";
	public static final String SCHEDULE_FILTER_STATE = "scheduleFilterState";

	private static List<String> fieldNames = new ArrayList<String>();

	static {
		fieldNames.add(SCHEDULE_FILTER_MONTH);
		fieldNames.add(SCHEDULE_FILTER_YEAR);
		fieldNames.add(SCHEDULE_FILTER_EMPLOYEE);
		fieldNames.add(SCHEDULE_FILTER_FROM);
		fieldNames.add(SCHEDULE_FILTER_TO);
		fieldNames.add(SCHEDULE_FILTER_TYPE);
		fieldNames.add(SCHEDULE_FILTER_STATE);
	}

	public ScheduleFilter() {
		super(ScheduleFilter.fieldNames);
	}
}
