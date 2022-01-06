package cz.softinel.retra.core.blo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.softinel.uaf.messages.MessagesSupport;

/**
 * Convenient base class for business logic objects.
 *
 * @version $Revision: 1.1 $ $Date: 2007-02-23 12:16:27 $
 * @author Pavel Mueller
 */
public abstract class AbstractLogicBean extends MessagesSupport {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
