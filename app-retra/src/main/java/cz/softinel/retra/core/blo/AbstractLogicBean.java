package cz.softinel.retra.core.blo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.softinel.uaf.messages.MessagesSupport;

/**
 * Convenient base class for business logic objects.
 *
 * @version $Revision: 1.1 $ $Date: 2007-02-23 12:16:27 $
 * @author Pavel Mueller
 */
public abstract class AbstractLogicBean extends MessagesSupport {
	protected Log logger = LogFactory.getLog(this.getClass());

}
