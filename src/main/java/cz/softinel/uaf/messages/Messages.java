package cz.softinel.uaf.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

/**
 * This is global class for messages. It contains infos, warnings, errors and
 * method for adding this messages to it.
 * 
 * @version $Revision: 1.4 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public class Messages implements Serializable {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final long serialVersionUID = 5619002213069950694L;

	private MessageSource messageSource;

	private List<Message> errors = new ArrayList<Message>();
	private List<Message> warnings = new ArrayList<Message>();
	private List<Message> infos = new ArrayList<Message>();

	/**
	 * Constructor
	 * 
	 * @param messageSource
	 */
	public Messages(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Returns list of all errors
	 * 
	 * @return
	 */
	public List<Message> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	/**
	 * Returns list of all warnings
	 * 
	 * @return
	 */
	public List<Message> getWarnings() {
		return Collections.unmodifiableList(warnings);
	}

	/**
	 * Returns list of all infos
	 * 
	 * @return
	 */
	public List<Message> getInfos() {
		return Collections.unmodifiableList(infos);
	}

	/**
	 * Add message to errors
	 * 
	 * @param error
	 */
	public void addError(Message error) {
		addMessage(errors, error);
	}

	/**
	 * Remove message from errors
	 * 
	 * @param error
	 */
	public void removeError(Message error) {
		errors.remove(error);
	}

	/**
	 * Add message to warnings
	 * 
	 * @param error
	 */
	public void addWarning(Message warning) {
		addMessage(warnings, warning);
	}

	/**
	 * Remove message from warnings
	 * 
	 * @param warning
	 */
	public void removeWarning(Message warning) {
		warnings.remove(warning);
	}

	/**
	 * Add message to infos
	 * 
	 * @param info
	 */
	public void addInfo(Message info) {
		addMessage(infos, info);
	}

	/**
	 * Remove message from infors
	 * 
	 * @param info
	 */
	public void removeInfo(Message info) {
		infos.remove(info);
	}

	/**
	 * Add all messages in parameter into errors
	 * 
	 * @param errors
	 */
	public void addErrors(List<Message> errors) {
		addAllMessages(errors, this.errors);
	}

	/**
	 * Add all mesasges in parameter into warnings
	 * 
	 * @param warnings
	 */
	public void addWarnings(List<Message> warnings) {
		addAllMessages(warnings, this.warnings);
	}

	/**
	 * Add all mesasges in parameter into infos
	 * 
	 * @param infos
	 */
	public void addInfos(List<Message> infos) {
		addAllMessages(infos, this.infos);
	}

	/**
	 * Add all messages in parameter to this messages
	 * 
	 * @param addedMessages
	 */
	public void addAllMessages(Messages addedMessages) {
		errors.addAll(addedMessages.getErrors());
		warnings.addAll(addedMessages.getWarnings());
		infos.addAll(addedMessages.getInfos());
	}

	/**
	 * This method add message into list and during adding it convert it - means get
	 * from resources, replace parameters etc.
	 * 
	 * @param destination
	 * @param message
	 */
	private void addMessage(List<Message> destination, Message message) {
		// message do not have defined value
		if (!StringUtils.hasText(message.getValue())) {
			String text = null;
			// try to get message from resource and replace parameters
			try {
				text = messageSource.getMessage(message.getValueOrKey(), message.getParameters(),
						message.getDefaultValue(), message.getLocale());
			}
			// could not find it in resource, never mind
			catch (Exception e) {
				logger.info("Couldn't find message for key " + message.getValueOrKey() + ". Using default value.");
			}

			// text-value is not set
			if (text == null) {
				// we have default value so put it in message as value
				if (message.getDefaultValue() != null) {
					text = message.getValueOrKey();
				}
				// we do not have default value so put valueOrKey into message value
				else {
					text = message.getValueOrKey();
				}
			}

			// set value
			message.setValue(text);
		}

		destination.add(message);
	}

	/**
	 * Add all messages in source into destination.
	 * 
	 * @param source
	 * @param destination
	 */
	private void addAllMessages(List<Message> source, List<Message> destination) {
		for (Message message : source) {
			addMessage(destination, message);
		}
	}

	public boolean hasErrors() {
		return errors.size() > 0;
	}

	public boolean hasWarnings() {
		return warnings.size() > 0;
	}

	public boolean hasInfos() {
		return infos.size() > 0;
	}

	public boolean hasWarningsOrErrors() {
		return hasErrors() || hasWarnings();
	}

	public boolean hasSomeMessage() {
		return hasErrors() || hasWarnings() || hasInfos();
	}

}
