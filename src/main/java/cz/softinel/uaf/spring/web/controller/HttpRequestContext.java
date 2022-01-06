package cz.softinel.uaf.spring.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

import cz.softinel.uaf.messages.DefaultMessagesContext;
import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;
import cz.softinel.uaf.messages.MessagesContext;

/**
 * Http request context implementation for context.
 *
 * @version $Revision: 1.5 $ $Date: 2007-02-23 12:31:03 $
 * @author Radek Pinc, Petr Sígl
 */
public class HttpRequestContext extends RequestContext {

	public static final String REQUEST_MESSAGES_KEY = "requestMessages";

	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSessionContext sessionContext = null;

	private MessagesContext messagesContext = new DefaultMessagesContext();

	public HttpRequestContext(HttpServletRequest request, HttpServletResponse response) {
		super("NOT_IMPLEMENTED");
		this.request = request;
		this.response = response;

		MessageSource messageSource = RequestContextUtils.getWebApplicationContext(request);
		Messages messages = new Messages(messageSource);
		messagesContext.setMessages(messages);

		// add messages from session to request
		prepareSessionMessages();

		this.request.setAttribute(REQUEST_MESSAGES_KEY, messages);
	}

	/**
	 * Sets a custom messages context. Default is {@link DefaultMessagesContext}.
	 * 
	 * @param messagesContext the messagesContext to set
	 */
	public void setMessagesContext(MessagesContext messagesContext) {
		this.messagesContext = messagesContext;
	}

	/**
	 * Returns a messages context.
	 * 
	 * @return the messagesContext
	 */
	public MessagesContext getMessagesContext() {
		return messagesContext;
	}

	@Override
	public Context getApplicationContext() {
		throw new IllegalStateException("HTTP Application context is not implemented yet.");
	}

	@Override
	public Object getAttribute(String key) {
		return request.getAttribute(key);
	}

	@Override
	public String getParameter(String name) {
		return request.getParameter(name);
	}

	@Override
	public Context getSessionContext() {
		if (sessionContext == null) {
			sessionContext = new HttpSessionContext(request);
		}
		return sessionContext;
	}

	@Override
	public Context getUseCaseContext() {
		throw new IllegalStateException("Use case context is not implemented yet.");
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.Context#getMessages()
	 */
	public Messages getMessages() {
		return messagesContext.getMessages();
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.RequestContext#getCookies()
	 */
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.RequestContext#addCookie(javax.servlet.http.Cookie)
	 */
	public void addCookie(Cookie cookie) {
		response.addCookie(cookie);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.Context#addError(cz.softinel.uaf.messages.Message)
	 */
	public void addError(Message error) {
		getMessages().addError(error);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.RequestContext#addRedirectIgnoreError(cz.softinel.uaf.messages.Message)
	 */
	public void addRedirectIgnoreError(Message redirectIgnoreError) {
		// if message has not set count to expire more than 0, must be set to 1 (to
		// ignore redirect)
		if (redirectIgnoreError.getCountToExpire() <= 0) {
			redirectIgnoreError.setCountToExpire(1);
		}
		getSessionContext().addError(redirectIgnoreError);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.Context#addErrors(java.util.List)
	 */
	public void addErrors(List<Message> errors) {
		getMessages().addErrors(errors);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.RequestContext#addRedirectIgnoreErrors(java.util.List)
	 */
	public void addRedirectIgnoreErrors(List<Message> redirectIgnoreErrors) {
		for (Message error : redirectIgnoreErrors) {
			addRedirectIgnoreError(error);
		}
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.Context#addInfo(cz.softinel.uaf.messages.Message)
	 */
	public void addInfo(Message info) {
		getMessages().addInfo(info);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.RequestContext#addRedirectIgnoreInfo(cz.softinel.uaf.messages.Message)
	 */
	public void addRedirectIgnoreInfo(Message redirectIgnoreInfo) {
		// if message has not set count to expire more than 0, must be set to 1 (to
		// ignore redirect)
		if (redirectIgnoreInfo.getCountToExpire() <= 0) {
			redirectIgnoreInfo.setCountToExpire(1);
		}
		getSessionContext().addInfo(redirectIgnoreInfo);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.Context#addInfos(java.util.List)
	 */
	public void addInfos(List<Message> infos) {
		getMessages().addInfos(infos);
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.RequestContext#addRedirectIgnoreInfos(java.util.List)
	 */
	public void addRedirectIgnoreInfos(List<Message> redirectIgnoreInfos) {
		for (Message info : redirectIgnoreInfos) {
			addRedirectIgnoreInfo(info);
		}
	}

	/**
	 * @see cz.softinel.uaf.spring.web.controller.Context#addWarning(cz.softinel.uaf.messages.Message)
	 */
	public void addWarning(Message warning) {
		getMessages().addWarning(warning);
	}

	public void addRedirectIgnoreWarning(Message redirectIgnoreWarning) {
		// if message has not set count to expire more than 0, must be set to 1 (to
		// ignore redirect)
		if (redirectIgnoreWarning.getCountToExpire() <= 0) {
			redirectIgnoreWarning.setCountToExpire(1);
		}
		getSessionContext().addWarning(redirectIgnoreWarning);
	}

	public void addWarnings(List<Message> warnings) {
		getMessages().addWarnings(warnings);
	}

	public void addRedirectIgnoreWarnings(List<Message> redirectIgnoreWarnings) {
		for (Message warning : redirectIgnoreWarnings) {
			addRedirectIgnoreWarning(warning);
		}
	}

	public List<Message> getErrors() {
		return getMessages().getErrors();
	}

	public List<Message> getInfos() {
		return getMessages().getInfos();
	}

	public List<Message> getWarnings() {
		return getMessages().getWarnings();
	}

	public void addAllMessages(Messages addedMessages) {
		getMessages().addAllMessages(addedMessages);
	}

	public void addAllRedirectIgnoreMessages(Messages addedMessages) {
		addRedirectIgnoreErrors(addedMessages.getErrors());
		addRedirectIgnoreWarnings(addedMessages.getWarnings());
		addRedirectIgnoreInfos(addedMessages.getInfos());
	}

	private void prepareSessionMessages() {
		Messages sessionMessages = getSessionContext().getMessages();

		List<Message> errors = new ArrayList<Message>(sessionMessages.getErrors());
		for (Message error : errors) {
			int countToExpire = error.getCountToExpire();
			if (countToExpire > 0) {
				addError(error);
				error.setCountToExpire(--countToExpire);
			}

			if (countToExpire <= 0) {
				sessionMessages.removeError(error);
			}
		}

		List<Message> warnings = new ArrayList<Message>(sessionMessages.getWarnings());
		for (Message warning : warnings) {
			addWarning(warning);
			int countToExpire = warning.getCountToExpire();
			if (countToExpire > 0) {
				warning.setCountToExpire(--countToExpire);
			}

			if (countToExpire <= 0) {
				sessionMessages.removeWarning(warning);
			}
		}

		List<Message> infos = new ArrayList<Message>(sessionMessages.getInfos());
		for (Message info : infos) {
			addInfo(info);
			int countToExpire = info.getCountToExpire();
			if (countToExpire > 0) {
				info.setCountToExpire(--countToExpire);
			}

			if (countToExpire <= 0) {
				sessionMessages.removeInfo(info);
			}
		}
	}

	/**
	 * Convers all current request messages to redirect ignoring messages.
	 */
	public void convertMessagesToRedirectIgnoring() {
		addAllRedirectIgnoreMessages(getMessages());
	}
}
