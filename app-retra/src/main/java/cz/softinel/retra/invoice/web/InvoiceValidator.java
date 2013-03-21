package cz.softinel.retra.invoice.web;

import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.convertor.ConvertException;
import cz.softinel.retra.core.utils.convertor.DateConvertor;
import cz.softinel.retra.core.utils.validator.CommonValidator;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.security.blo.MiraSecurityLogic;


public abstract class InvoiceValidator implements Validator {
	
	private InvoiceLogic invoiceLogic;
	private MiraSecurityLogic securityLogic;

	public boolean supports(Class clazz) {
		return clazz.equals(InvoiceForm.class);
	}

	public void validate(Object command, Errors errors) {
		if (!invoiceLogic.isCodeGenerated()) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "invoice.error.require.code");
			String code = (String) errors.getFieldValue("code");
			if (!(code == null || code.length() == 0)) {
				validateCodeUniqness(errors,code);
			}
		} else {
			validateSequence(errors);
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "orderDate", "invoice.error.require.orderDate");
		CommonValidator.validateDate("orderDate", errors, "invoice.error.bad.format.orderDate", null);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "finishDate", "invoice.error.require.finishDate");
		CommonValidator.validateDate("finishDate", errors, "invoice.error.bad.format.finishDate", null);

		validateFinishGreaterThanOrder(errors);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "invoice.error.require.name");
	}
	
	protected abstract void validateCodeUniqness(Errors errors, String code);
	
	protected abstract void validateSequence(Errors errors);
	
	public InvoiceLogic getInvoiceLogic() {
		return invoiceLogic;
	}

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	public MiraSecurityLogic getSecurityLogic() {
		return securityLogic;
	}

	public void setSecurityLogic(MiraSecurityLogic securityLogic) {
		this.securityLogic = securityLogic;
	}
	
	private void validateFinishGreaterThanOrder(Errors errors){
		Date order = null;
		Date finish = null;
		try {
			String orders = (String)errors.getFieldValue("orderDate");
			String finishs = (String)errors.getFieldValue("finishDate");

			order = DateConvertor.convertToDateFromDateString(orders);
			finish = DateConvertor.convertToDateFromDateString(finishs);
		}
		catch (ConvertException e) {
			//couldn't compare return
			return;
		}
		
		if (order.getTime() >= finish.getTime()) {
			errors.reject("invoiceForm.finish.greater.order");
		}
	}

}
