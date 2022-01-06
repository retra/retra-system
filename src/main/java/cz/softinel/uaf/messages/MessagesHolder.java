package cz.softinel.uaf.messages;

/**
 * Holder for the messages object using a thread local variable. It is possible
 * to use it directly, but consider using {@link DefaultMessagesContext}.
 *
 * @version $Revision: 1.1 $ $Date: 2007-02-23 12:16:27 $
 * @author Pavel Mueller
 * @see DefaultMessagesContext
 */
public final class MessagesHolder {

	private static ThreadLocal<Messages> holder = new InheritableThreadLocal<Messages>();

	/**
	 * No instances of utility classes
	 */
	private MessagesHolder() {
	}

	/**
	 * @see cz.softinel.uaf.messages.MessagesContext#getMessages()
	 */
	public static Messages getMessages() {
		return holder.get();
	}

	/**
	 * @see cz.softinel.uaf.messages.MessagesContext#setMessages(cz.softinel.uaf.messages.Messages)
	 */
	public static void setMessages(Messages messages) {
		holder.set(messages);
	}

	/**
	 * @see cz.softinel.uaf.messages.MessagesContext#clearMessages()
	 */
	public static void clearMessages() {
		holder.set(null);
	}

}
