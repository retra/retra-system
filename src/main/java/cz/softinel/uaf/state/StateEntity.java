package cz.softinel.uaf.state;

import java.io.Serializable;

/**
 * This interface must implements all entities, which have defined state cycle.
 *
 * @version $Revision: 1.1 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public interface StateEntity extends Serializable {

	/**
	 * Deleted state
	 */
	public static final int STATE_DELETED = 0;

	/**
	 * Active state
	 */
	public static final int STATE_ACTIVE = 1;

	/**
	 * Active state
	 */
	public static final int STATE_CLOSED = 100;

	/**
	 * This method is used for get actual state
	 * 
	 * @return
	 */
	public int getState();

	/**
	 * This method is used for set actual state.
	 * 
	 * @param state
	 */
	public void setState(int state);

}
