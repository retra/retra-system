package cz.softinel.uaf.spring.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;

public class HttpSessionContext implements Context {
	
	public static final String SESSION_MESSAGES_KEY = "sessionMessages";

	private final HttpSession session;
	
	Messages messages = null;
	
	public HttpSessionContext(HttpServletRequest request) {
		this.session = request.getSession();
		
		messages = (Messages) session.getAttribute(SESSION_MESSAGES_KEY);
		if (messages == null) {
			MessageSource messageSource = RequestContextUtils.getWebApplicationContext(request);
			messages = new Messages(messageSource);
			session.setAttribute(SESSION_MESSAGES_KEY, messages);
		}
	}
	
	public String id() {
		return session.getId();
	}

	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}

	public void setAttribute(String key, Object value) {
		session.setAttribute(key, value);
	}
	
	public Messages getMessages() {
		return messages;
	}
	
	public void addError(Message error) {
		messages.addError(error);
	}

	public void addErrors(List<Message> errors) {
		messages.addErrors(errors);
	}

	public void addInfo(Message info) {
		messages.addInfo(info);
	}

	public void addInfos(List<Message> infos) {
		messages.addInfos(infos);
	}

	public void addWarning(Message warning) {
		messages.addWarning(warning);
	}

	public void addWarnings(List<Message> warnings) {
		messages.addWarnings(warnings);
	}

	public List<Message> getErrors() {
		return messages.getErrors();
	}

	public List<Message> getInfos() {
		return messages.getInfos();
	}

	public List<Message> getWarnings() {
		return messages.getWarnings();
	}
	
	public void addAllMessages(Messages addedMessages) {
		messages.addAllMessages(addedMessages);
	}
}