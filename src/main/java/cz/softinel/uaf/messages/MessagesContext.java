package cz.softinel.uaf.messages;

import java.io.Serializable;

/**
 * Context for holding {@link Messages} object.
 * Use this context to access current messages.
 * 
 * <p>The default implementation {@link DefaultMessagesContext} uses
 * a thread local variable to hold the current messages object. 
 * For remoting purposes there would have to be an extension to a remote
 * invocation to transfer the messages and to set it to the remote thread local.
 *
 * @version $Revision: 1.2 $ $Date: 2007-11-26 18:50:54 $
 * @author Pavel Mueller
 * @see DefaultMessagesContext
 * @see MessagesHolder
 */
public interface MessagesContext extends Serializable {
	
	/**
	 * Returns current messages.
	 * @return current messages, <code>null</code> if no messages set
	 */
	public Messages getMessages();
	
	/**
	 * Sets current messages to the context.
	 * @param messages current messages
	 */
	public void setMessages(Messages messages);
	
	/**
	 * Removes messages object from the context.
	 */
	public void clearMessages();

}
