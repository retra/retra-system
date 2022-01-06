package cz.softinel.uaf.messages;

import java.util.List;

/**
 * Convenient base base support class which provides methods for accessing
 * messages object.
 * 
 * Set a custom {@link MessagesContext} if you need an implementation other than
 * based on a thread local variable.
 *
 * @version $Revision: 1.1 $ $Date: 2007-02-23 12:16:27 $
 * @author Pavel Mueller
 */
public abstract class MessagesSupport {

	private MessagesContext messagesContext = new DefaultMessagesContext();

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

	public Messages getMessages() {
		return messagesContext.getMessages();
	}

	public void addError(Message error) {
		getMessages().addError(error);
	}

	public void addErrors(List<Message> errors) {
		getMessages().addErrors(errors);
	}

	public void addInfo(Message info) {
		getMessages().addInfo(info);
	}

	public void addInfos(List<Message> infos) {
		getMessages().addInfos(infos);
	}

	public void addWarning(Message warning) {
		getMessages().addWarning(warning);
	}

	public void addWarnings(List<Message> warnings) {
		getMessages().addWarnings(warnings);
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

}
