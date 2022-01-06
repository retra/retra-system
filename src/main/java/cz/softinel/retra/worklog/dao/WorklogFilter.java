package cz.softinel.retra.worklog.dao;

import java.util.ArrayList;
import java.util.List;

import cz.softinel.uaf.filter.EntityFilter;

public class WorklogFilter extends EntityFilter {

	public static final String WORKLOG_FILTER_MONTH = "worklogFilterMonth";
	public static final String WORKLOG_FILTER_YEAR = "worklogFilterYear";
	public static final String WORKLOG_FILTER_EMPLOYEE = "worklogFilterEmployee";
	public static final String WORKLOG_FILTER_DATE = "worklogFilterDate";
	public static final String WORKLOG_FILTER_WEEK = "worklogFilterWeek";
	public static final String WORKLOG_FILTER_FROM = "worklogFilterFrom";
	public static final String WORKLOG_FILTER_TO = "worklogFilterTo";
	public static final String WORKLOG_FILTER_ACTIVITY = "worklogFilterActivity";
	public static final String WORKLOG_FILTER_PROJECT = "worklogFilterProject";
//	public static final String WORKLOG_FILTER_MAIN_PROJECT = "worklogFilterMainProject";
	public static final String WORKLOG_FILTER_DESCRIPTION = "worklogFilterDescription";
	public static final String WORKLOG_FILTER_ON_INVOICE = "worklogFilterOnInvoice";
	public static final String WORKLOG_FILTER_INVOICE_CODE = "worklogFilterInvoiceCode";
	public static final String WORKLOG_FILTER_INVOICE_RELATION = "worklogFilterInvoiceRelation";

	private static List<String> fieldNames = new ArrayList<String>();

	static {
		fieldNames.add(WORKLOG_FILTER_MONTH);
		fieldNames.add(WORKLOG_FILTER_YEAR);
		fieldNames.add(WORKLOG_FILTER_EMPLOYEE);
		fieldNames.add(WORKLOG_FILTER_DATE);
		fieldNames.add(WORKLOG_FILTER_WEEK);
		fieldNames.add(WORKLOG_FILTER_FROM);
		fieldNames.add(WORKLOG_FILTER_TO);
		fieldNames.add(WORKLOG_FILTER_ACTIVITY);
		fieldNames.add(WORKLOG_FILTER_PROJECT);
//		fieldNames.add(WORKLOG_FILTER_MAIN_PROJECT);
		fieldNames.add(WORKLOG_FILTER_DESCRIPTION);
		fieldNames.add(WORKLOG_FILTER_ON_INVOICE);
		fieldNames.add(WORKLOG_FILTER_INVOICE_CODE);
		fieldNames.add(WORKLOG_FILTER_INVOICE_RELATION);
	}

	public WorklogFilter() {
		super(WorklogFilter.fieldNames);
	}
}
