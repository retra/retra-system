package cz.softinel.retra.invoice.dao;

import java.util.ArrayList;
import java.util.List;

import cz.softinel.uaf.filter.EntityFilter;

public class InvoiceFilter extends EntityFilter {

	public static final String INVOICE_FILTER_EMPLOYEE = "invoiceFilterEmployee";
	public static final String INVOICE_FILTER_ORDER_DATE_FROM = "invoiceFilterOrderDateFrom";
	public static final String INVOICE_FILTER_ORDER_DATE_TO = "invoiceFilterOrderDateTo";
	public static final String INVOICE_FILTER_START_DATE_FROM = "invoiceFilterStartDateFrom";
	public static final String INVOICE_FILTER_START_DATE_TO = "invoiceFilterStartDateTo";
	public static final String INVOICE_FILTER_FINISH_DATE_FROM = "invoiceFilterFinishDateFrom";
	public static final String INVOICE_FILTER_FINISH_DATE_TO = "invoiceFilterFinishDateTo";
	public static final String INVOICE_FILTER_CODE = "invoiceFilterCode";
	public static final String INVOICE_FILTER_STATE = "invoiceFilterState";

	private static List<String> fieldNames = new ArrayList<String>();

	static {
		fieldNames.add(INVOICE_FILTER_EMPLOYEE);
		fieldNames.add(INVOICE_FILTER_ORDER_DATE_FROM);
		fieldNames.add(INVOICE_FILTER_ORDER_DATE_TO);
		fieldNames.add(INVOICE_FILTER_START_DATE_FROM);
		fieldNames.add(INVOICE_FILTER_START_DATE_TO);
		fieldNames.add(INVOICE_FILTER_FINISH_DATE_FROM);
		fieldNames.add(INVOICE_FILTER_FINISH_DATE_TO);
		fieldNames.add(INVOICE_FILTER_CODE);
		fieldNames.add(INVOICE_FILTER_STATE);
	}

	public InvoiceFilter() {
		super(InvoiceFilter.fieldNames);
	}
}
