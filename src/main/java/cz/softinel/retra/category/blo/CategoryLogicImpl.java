package cz.softinel.retra.category.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.category.Category;
import cz.softinel.retra.category.dao.CategoryDao;
import cz.softinel.retra.core.blo.AbstractLogicBean;

/**
 * Implementation of category logic
 * 
 * @version $Revision: 1.5 $ $Date: 2007-04-03 07:55:39 $
 * @author Radek Pinc, Petr Sígl
 */
public class CategoryLogicImpl extends AbstractLogicBean implements CategoryLogic {

	private CategoryDao categoryDao;

	/**
	 * @return the categoryDao
	 */
	public CategoryDao getCategoryDao() {
		return categoryDao;
	}

	/**
	 * @param categoryDao the categoryDao to set
	 */
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	/**
	 * @see cz.softinel.retra.category.blo.CategoryLogic#findAllCategories()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Category> findAllCategories() {
		return categoryDao.selectAll();
	}

	/**
	 * @see cz.softinel.retra.category.blo.CategoryLogic#findAllNotDeletedCategories()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Category> findAllNotDeletedCategories() {
		return categoryDao.selectAllNotDeleted();
	}
}
