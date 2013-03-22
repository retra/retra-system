package cz.softinel.uaf.spring.web.controller;

import java.util.List;

import cz.softinel.uaf.messages.Message;
import cz.softinel.uaf.messages.Messages;

public interface Context {

	public String id();
	public Object getAttribute(String key);
	public void setAttribute(String key, Object value);

	public Messages getMessages();
	public void addAllMessages(Messages messages);
	
	public List<Message> getErrors();
	public List<Message> getWarnings();
	public List<Message> getInfos();

	public void addError(Message error);
	public void addWarning(Message warning);
	public void addInfo(Message info);

	public void addErrors(List<Message> errors);
	public void addWarnings(List<Message> warnings);
	public void addInfos(List<Message> infos);
}
