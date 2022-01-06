package cz.softinel.retra.activity.dao;

import java.util.List;

import cz.softinel.retra.activity.Activity;

/**
 * Dao for worklog activities
 *
 * @version $Revision: 1.2 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public interface ActivityDao {

	/**
	 * Returns activity according to primary key.
	 * 
	 * @param pk primary key of activity
	 * @return
	 */
	public Activity get(Long pk);

	/**
	 * Insert Activity
	 * 
	 * @param activity to insert
	 */
	public Activity insert(Activity activity);

	/**
	 * Update activity
	 * 
	 * @param activity to update
	 */
	public void update(Activity activity);

	/**
	 * Delete activity
	 * 
	 * @param activity to delete
	 */
	public void delete(Activity activity);

	/**
	 * Returns all activities
	 * 
	 * @return all activities
	 */
	public List<Activity> selectAll();

	/**
	 * Returns all activities, which are not deleted
	 * 
	 * @return all activities
	 */
	public List<Activity> selectAllNotDeleted();

	/**
	 * Load informations about activity (defined by pk)
	 * 
	 * @param activity where load and where is pk set
	 */
	public void load(Activity activity);
}
