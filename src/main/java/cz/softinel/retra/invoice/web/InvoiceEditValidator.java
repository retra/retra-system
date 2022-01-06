package cz.softinel.retra.invoice.web;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.invoice.Invoice;

public class InvoiceEditValidator extends InvoiceValidator implements Validator {

	protected void validateCodeUniqness(Errors errors, String code) {
		String pk = (String) errors.getFieldValue("pk");
		Long pkLong = LongConvertor.getLongFromString(pk);

		Employee employee = getSecurityLogic().getLoggedEmployee();

		List<Invoice> invoices = getInvoiceLogic().findInvoicesForEmployeeWithCode(employee.getPk(), code);
		if (invoices.size() > 1) {
			errors.reject("invoice.create.invoice.exists");
		} else if (invoices.size() == 1) {
			if (!invoices.get(0).getPk().equals(pkLong)) {
				errors.reject("invoice.create.invoice.exists");
			}
		}
	}

	@Override
	protected void validateSequence(Errors errors) {
		// do nothing sequence on edit is not allowed
	}
}
