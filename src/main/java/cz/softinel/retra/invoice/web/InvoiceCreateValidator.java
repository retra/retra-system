package cz.softinel.retra.invoice.web;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.invoice.Invoice;

public class InvoiceCreateValidator extends InvoiceValidator implements Validator {

	protected void validateCodeUniqness(Errors errors, String code) {
		Employee employee = getSecurityLogic().getLoggedEmployee();

		// TODO : the query can be optimized with obtaining only the count
		List<Invoice> invoices = getInvoiceLogic().findInvoicesForEmployeeWithCode(employee.getPk(), code);
		if (invoices.size() > 0) {
			errors.reject("invoice.create.invoice.exists");
		}
	}

	@Override
	protected void validateSequence(Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sequence", "invoice.error.require.sequence");

	}
}
