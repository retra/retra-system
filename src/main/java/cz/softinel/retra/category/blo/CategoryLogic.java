package cz.softinel.retra.category.blo;

import java.util.List;

import cz.softinel.retra.category.Category;

/**
 * This class represents all logic for categories.
 *
 * @version $Revision: 1.3 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public interface CategoryLogic {

	/**
	 * Find all category items.
	 * 
	 * @return
	 */
	public List<Category> findAllCategories();

	/**
	 * Find all category items, which are not deleted.
	 * 
	 * @return
	 */
	public List<Category> findAllNotDeletedCategories();
}
