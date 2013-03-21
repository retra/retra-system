package cz.softinel.retra.activity.blo;

import java.util.List;

import cz.softinel.retra.activity.Activity;

/**
 * This class represents all logic for activities.
 *
 * @version $Revision: 1.3 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public interface ActivityLogic {
	
	/**
	 * Find all activity items.
	 * 
	 * @return
	 */
	public List<Activity> findAllActivities();	

	/**
	 * Find all activity items, which are not deleted.
	 * 
	 * @return
	 */
	public List<Activity> findAllNotDeletedActivities();	
}
