package cz.softinel.retra.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.invoice.web.InvoiceForm;
import cz.softinel.retra.invoice.web.InvoiceRelationSelectValue;
import cz.softinel.retra.invoice.web.InvoiceStateSelectValue;

public class InvoiceHelper {

	public static final String INVOICE_RELATION_DO_NOT_CARE = "doNotCare";
	public static final String INVOICE_RELATION_NOT_CLOSED = "notClosed";
	public static final String INVOICE_RELATION_CLOSED = "closed";

	public final static List<InvoiceRelationSelectValue> INVOICE_RELATIONS = new ArrayList<InvoiceRelationSelectValue>();
	public final static List<InvoiceStateSelectValue> INVOICE_STATES = new ArrayList<InvoiceStateSelectValue>();

	static {
		INVOICE_RELATIONS.add(new InvoiceRelationSelectValue(INVOICE_RELATION_DO_NOT_CARE));
		INVOICE_RELATIONS.add(new InvoiceRelationSelectValue(INVOICE_RELATION_NOT_CLOSED));
		INVOICE_RELATIONS.add(new InvoiceRelationSelectValue(INVOICE_RELATION_CLOSED));

		INVOICE_STATES.add(new InvoiceStateSelectValue(Invoice.STATE_ACTIVE));
		INVOICE_STATES.add(new InvoiceStateSelectValue(Invoice.STATE_CLOSED));
		INVOICE_STATES.add(new InvoiceStateSelectValue(Invoice.STATE_DELETED));
	}

	public static String getCodeAndName(Invoice invoice) {
		StringBuffer sb = new StringBuffer();
		sb.append(invoice.getCode());
		sb.append(" - ");
		sb.append(invoice.getName());
		return sb.toString().trim();
	}

	public static void formToEntity(InvoiceForm form, Invoice entity, boolean isCodeGenerated) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}

		Date orderDate = DateConvertor.getDateFromDateString(form.getOrderDate());
		entity.setOrderDate(orderDate);

		Date finishDate = DateConvertor.getDateFromDateString(form.getFinishDate());
		entity.setFinishDate(finishDate);

		entity.setName(form.getName());

		if (!isCodeGenerated) {
			entity.setCode(form.getCode());
		}
	}

	public static void entityToForm(Invoice entity, InvoiceForm form) {

		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));

		String orderDate = DateConvertor.convertToDateStringFromDate(entity.getOrderDate());
		form.setOrderDate(orderDate);

		String finishDate = DateConvertor.convertToDateStringFromDate(entity.getFinishDate());
		form.setFinishDate(finishDate);

		form.setName(entity.getName());
		form.setCode(entity.getCode());
	}

}
