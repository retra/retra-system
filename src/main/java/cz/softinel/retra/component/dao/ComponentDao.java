package cz.softinel.retra.component.dao;

import java.util.List;

import cz.softinel.retra.component.Component;

/**
 * Dao for worklog components
 *
 * @version $Revision: 1.3 $ $Date: 2007-08-19 19:51:55 $
 * @author Petr Sígl
 */
public interface ComponentDao {

	/**
	 * Returns component according to primary key.
	 * 
	 * @param pk primary key of component
	 * @return
	 */
	public Component get(Long pk);

	/**
	 * Insert Component
	 * 
	 * @param component to insert
	 */
	public Component insert(Component component);

	/**
	 * Update component
	 * 
	 * @param component to update
	 */
	public void update(Component component);

	/**
	 * Delete component
	 * 
	 * @param component to delete
	 */
	public void delete(Component component);

	/**
	 * Returns all components
	 * 
	 * @return all components
	 */
	public List<Component> selectAll();

	/**
	 * Returns all components which are not deleted
	 * 
	 * @return all components
	 */
	public List<Component> selectAllNotDeleted();

	/**
	 * Load informations about component (defined by pk)
	 * 
	 * @param component where load and where is pk set
	 */
	public void load(Component component);

	public List<Component> selectComponentsWithCode(String code);
}
