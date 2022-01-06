package cz.softinel.uaf.messages;

/**
 * Default messages context implementation based on a thread local variable.
 *
 * @version $Revision: 1.2 $ $Date: 2007-11-28 23:05:12 $
 * @author Pavel Mueller
 * @see MessagesHolder
 */
public class DefaultMessagesContext implements MessagesContext {

	/**
	 * @see cz.softinel.uaf.messages.MessagesContext#getMessages()
	 */
	public Messages getMessages() {
		return MessagesHolder.getMessages();
	}

	/**
	 * @see cz.softinel.uaf.messages.MessagesContext#setMessages(cz.softinel.uaf.messages.Messages)
	 */
	public void setMessages(Messages messages) {
		MessagesHolder.setMessages(messages);
	}

	/**
	 * @see cz.softinel.uaf.messages.MessagesContext#clearMessages()
	 */
	public void clearMessages() {
		MessagesHolder.clearMessages();
	}

}