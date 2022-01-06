package cz.softinel.retra.type.dao;

import java.util.List;

import cz.softinel.retra.type.Type;

/**
 * Dao for worklog types
 *
 * @version $Revision: 1.1 $ $Date: 2007-01-29 07:11:43 $
 * @author Petr Sígl
 */
public interface TypeDao {

	/**
	 * Returns type according to primary key.
	 * 
	 * @param pk primary key of type
	 * @return
	 */
	public Type get(Long pk);

	/**
	 * Insert Type
	 * 
	 * @param type to insert
	 */
	public Type insert(Type type);

	/**
	 * Update type
	 * 
	 * @param type to update
	 */
	public void update(Type type);

	/**
	 * Delete type
	 * 
	 * @param type to delete
	 */
	public void delete(Type type);

	/**
	 * Returns all types
	 * 
	 * @return all types
	 */
	public List<Type> selectAll();

	/**
	 * Load informations about type (defined by pk)
	 * 
	 * @param type where load and where is pk set
	 */
	public void load(Type type);
}
