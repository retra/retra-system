package cz.softinel.uaf.spring.web.controller;

import java.util.List;

import javax.servlet.http.Cookie;

import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;

public abstract class RequestContext implements Context {

	private final String id;

	public RequestContext(String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}

	public abstract Object getAttribute(String key);

	public void setAttribute(String key, Object value) {
		throw new UnsupportedOperationException("Set request attribure is not permited. Use Model instead.");
	}

	/** Returns application context */
	public abstract Context getApplicationContext();

	/** Returns session context of this request */
	public abstract Context getSessionContext();

	/** Returns use case context of this request */
	public abstract Context getUseCaseContext();

	/** Returns request parameter by name */
	public abstract String getParameter(String name);

	public Cookie[] getCookies() {
		throw new UnsupportedOperationException("Method getCookies() is not implemented.");
	}

	public void addCookie(Cookie cookie) {
		throw new UnsupportedOperationException("Method addCookie() is not implemented.");
	}

	public void addRedirectIgnoreError(Message redirectIgnoreError) {
		throw new UnsupportedOperationException("Method addRedirectIgnoreError() is not implemented.");
	}

	public void addRedirectIgnoreInfo(Message redirectIgnoreInfo) {
		throw new UnsupportedOperationException("Method addRedirectIgnoreInfo() is not implemented.");
	}

	public void addRedirectIgnoreWarning(Message redirectIgnoreWarning) {
		throw new UnsupportedOperationException("Method addRedirectIgnoreWarning() is not implemented.");
	}

	public void addRedirectIgnoreErrors(List<Message> redirectIgnoreErrors) {
		throw new UnsupportedOperationException("Method addRedirectIgnoreErrors() is not implemented.");
	}

	public void addRedirectIgnoreInfos(List<Message> redirectIgnoreInfos) {
		throw new UnsupportedOperationException("Method addRedirectIgnoreInfos() is not implemented.");
	}

	public void addRedirectIgnoreWarnings(List<Message> redirectIgnoreWarnings) {
		throw new UnsupportedOperationException("Method addRedirectIgnoreWarnings() is not implemented.");
	}

	public void addAllMessages(Messages messages) {
		throw new UnsupportedOperationException("Method addAllMessages() is not implemented.");
	}

	public void addAllRedirectIgnoreMessages(Messages messages) {
		throw new UnsupportedOperationException("Method addAllRedirectIgnoreMessages() is not implemented.");
	}

	public void convertMessagesToRedirectIgnoring() {
		throw new UnsupportedOperationException("Method convertMessagesToRedirectIgnoring() is not implemented.");
	}
}
