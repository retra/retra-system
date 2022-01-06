package cz.softinel.retra.invoice.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.invoice.InvoiceHelper;
import cz.softinel.retra.invoice.blo.InvoiceLogic;
import cz.softinel.retra.invoice.dao.InvoiceFilter;
import cz.softinel.retra.spring.web.DispatchController;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogHelper;
import cz.softinel.retra.worklog.blo.WorklogLogic;
import cz.softinel.sis.security.PermissionHelper;
import cz.softinel.uaf.filter.Filter;
import cz.softinel.uaf.filter.FilterHelper;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

public class InvoiceController extends DispatchController {

	private InvoiceLogic invoiceLogic;
	private WorklogLogic worklogLogic;
	private EmployeeLogic employeeLogic;

	// Configuration setter methods ..

	public void setInvoiceLogic(InvoiceLogic invoiceLogic) {
		this.invoiceLogic = invoiceLogic;
	}

	public void setWorklogLogic(WorklogLogic worklogLogic) {
		this.worklogLogic = worklogLogic;
	}

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	@Override
	public Filter getFilter(Model model) {
		Filter filter = (Filter) model.get(InvoiceFilter.class.toString());
		if (filter == null) {
			filter = new InvoiceFilter();
			model.set(InvoiceFilter.class.toString(), filter);
		}
		return filter;
	}

	// Action methods ...

	/**
	 * Show invoice list.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView invoiceList(Model model, RequestContext requestContext) {
		// reset batch result
		requestContext.getSessionContext().setAttribute("invoiceBatchResult", null);

		Filter filter = getFilter(model);
		Long employeePk = FilterHelper.getFieldAsLong(InvoiceFilter.INVOICE_FILTER_EMPLOYEE, filter);

		// employee not set, so show actualy logged user
		if (employeePk == null) {
			employeePk = getSecurityLogic().getLoggedEmployee().getPk();
			FilterHelper.setField(InvoiceFilter.INVOICE_FILTER_EMPLOYEE, employeePk, filter);
		}

		Integer state = FilterHelper.getFieldAsInteger(InvoiceFilter.INVOICE_FILTER_STATE, filter);
		// state not set, so show actualy open
		if (state == null) {
			state = Invoice.STATE_ACTIVE;
			FilterHelper.setField(InvoiceFilter.INVOICE_FILTER_STATE, state, filter);
		}

		prepareEmployees(model);
		prepareInvoiceStates(model);

		List<Invoice> list = invoiceLogic.findByFilter(filter);
		model.set("invoices", list);

		return createModelAndView(model, getSuccessView());
	}

	/**
	 * Show invoice batch result.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView invoiceBatchResult(Model model, RequestContext requestContext) {
		List<Invoice> list = (List<Invoice>) requestContext.getSessionContext().getAttribute("invoiceBatchResult");
		if (list != null) {
			model.set("invoices", list);
			return createModelAndView(model, getSuccessView());
		}

		return createModelAndView(model, getFailureView());
	}

	/**
	 * View given invoice.
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView invoiceView(Model model, RequestContext requestContext) {
		String strPk = requestContext.getParameter("pk");
		Long pk = LongConvertor.getLongFromString(strPk);
		if (pk != null) {
			Invoice invoice = invoiceLogic.get(pk);
			model.put("invoice", invoice);

			boolean hasWorklogAdminPermission = getSecurityLogic()
					.hasPermission(PermissionHelper.PERMISSION_VIEW_ALL_WORKLOGS);

			List<Worklog> worklogs = new ArrayList<Worklog>();

			// can see worklog items
			if (hasWorklogAdminPermission
					|| getSecurityLogic().getLoggedEmployee().getPk().equals(invoice.getEmployee().getPk())) {
				worklogs = prepareWorklogItems(pk);
			} else {
				worklogs.add(WorklogHelper.getNoPermissionWorklog());
			}
			model.set("worklogs", worklogs);
		}
		return createModelAndView(model, getSuccessView());
	}

	/**
	 * Delete given invoice
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView invoiceDelete(Model model, RequestContext requestContext) {
		String invoicePkStr = requestContext.getParameter("pk");
		Long invoicePk = LongConvertor.getLongFromString(invoicePkStr);
		String view = getSuccessView();
		if (invoicePk != null) {
			Invoice invoice = invoiceLogic.get(invoicePk);
			invoiceLogic.remove(invoice);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView();
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("invoice.delete.success"));
			}

			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView();
		}
		return createModelAndView(model, view);
	}

	/**
	 * Unpair given worklog item from given invoice
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView invoiceWorklogUnpair(Model model, RequestContext requestContext) {
		String invoicePkStr = requestContext.getParameter("ipk");
		Long invoicePk = LongConvertor.getLongFromString(invoicePkStr);

		String worklogPkStr = requestContext.getParameter("wpk");
		Long worklogPk = LongConvertor.getLongFromString(worklogPkStr);

		String view = getSuccessView() + "&pk=" + invoicePkStr;
		if (invoicePk != null && worklogPk != null) {
			Worklog worklog = worklogLogic.get(worklogPk);
			Invoice invoice = invoiceLogic.get(invoicePk);

			worklogLogic.unpairWorklogWithInvoice(worklog, invoice);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView() + "&pk=" + invoicePkStr;
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("invoice.worklog.unpair.success"));
			}

			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView() + "&pk=" + invoicePkStr;
		}

		return createModelAndView(model, view);
	}

	/**
	 * Close given invoice
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView invoiceClose(Model model, RequestContext requestContext) {
		String invoicePkStr = requestContext.getParameter("pk");
		Long invoicePk = LongConvertor.getLongFromString(invoicePkStr);
		String view = getSuccessView();
		if (invoicePk != null) {
			Invoice invoice = invoiceLogic.get(invoicePk);
			invoiceLogic.close(invoice);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView();
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("invoice.close.success"));
			}
			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView();
		}
		return createModelAndView(model, view);
	}

	/**
	 * Close given invoice
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView invoiceOpen(Model model, RequestContext requestContext) {
		String invoicePkStr = requestContext.getParameter("pk");
		Long invoicePk = LongConvertor.getLongFromString(invoicePkStr);
		String view = getSuccessView();
		if (invoicePk != null) {
			Invoice invoice = invoiceLogic.get(invoicePk);
			invoiceLogic.open(invoice);
			if (requestContext.getErrors().size() > 0) {
				view = getErrorView();
			} else {
				requestContext.addRedirectIgnoreInfo(new Message("invoice.open.success"));
			}
			requestContext.convertMessagesToRedirectIgnoring();
		} else {
			view = getErrorView();
		}
		return createModelAndView(model, view);
	}

	/**
	 * Prepare list of worklog items on this invoice.
	 * 
	 * @param pk
	 * @return
	 */
	private List<Worklog> prepareWorklogItems(Long pk) {
		List<Worklog> worklogs = worklogLogic.findAllWorklogsForInvoice(pk);
		return worklogs;
	}

	/**
	 * Prepare all employess list.
	 * 
	 * @param model
	 */
	private void prepareEmployees(Model model) {
		List<Employee> employees = employeeLogic.getAllEmployees(true, true);
		model.set("employees", employees);
	}

	/**
	 * Prepare invoice states.
	 * 
	 * @param model
	 */
	private void prepareInvoiceStates(Model model) {
		model.set("states", InvoiceHelper.INVOICE_STATES);
	}

}
