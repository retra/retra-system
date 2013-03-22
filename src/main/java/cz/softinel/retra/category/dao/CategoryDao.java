package cz.softinel.retra.category.dao;

import java.util.List;

import cz.softinel.retra.category.Category;

/**
 * Dao for worklog activities
 *
 * @version $Revision: 1.2 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public interface CategoryDao {

	/**
	 * Returns category according to primary key. 
	 * 
	 * @param pk primary key of category
	 * @return
	 */
	public Category get(Long pk);
	
	/**
	 * Insert Category
	 * 
	 * @param category to insert
	 */
	public Category insert(Category category);

	/**
	 * Update category
	 * 
	 * @param category to update
	 */
	public void update(Category category);

	/**
	 * Delete category
	 * 
	 * @param category to delete
	 */
	public void delete(Category category);
	
	/**
	 * Returns all activities
	 * 
	 * @return all activities
	 */
	public List<Category> selectAll();

	/**
	 * Returns all activities, which are not deleted
	 * 
	 * @return all activities
	 */
	public List<Category> selectAllNotDeleted();

	/**
	 * Load informations about category (defined by pk)
	 * 
	 * @param category where load and where is pk set
	 */
	public void load(Category category);
}
