package cz.softinel.retra.invoice.web;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import cz.softinel.uaf.spring.web.controller.AbstractCookieHelper;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * Invoice cookie helper.
 *
 * @author Petr Sígl
 */
public class InvoiceCookieHelper extends AbstractCookieHelper {

	private static final String COOKIE_NAME_INVOICE_SEQUENCE = "retra.invoiceForm.sequence";

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
		InvoiceForm invoice = (InvoiceForm) commandForm;
		createAndAddCookie(COOKIE_NAME_INVOICE_SEQUENCE, invoice.getSequence(), requestContext);
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
		AbstractInvoiceForm invoice = (AbstractInvoiceForm) commandForm;
		Cookie[] cookies = requestContext.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				if (COOKIE_NAME_INVOICE_SEQUENCE.equals(name)) {
					invoice.setSequence(value);
					continue;
				}
			}
		}
	}

	public void importFromCookies(Object commandForm, HttpServletRequest request) {
		AbstractInvoiceForm invoice = (AbstractInvoiceForm) commandForm;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				if (COOKIE_NAME_INVOICE_SEQUENCE.equals(name)) {
					invoice.setSequence(value);
					continue;
				}
			}
		}
	}
}
