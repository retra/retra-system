package cz.softinel.retra.category.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import cz.softinel.retra.category.Category;

/**
 * This class is hibernate dependent implementation of CategoryDao  
 *
 * @version $Revision: 1.4 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public class HibernateCategoryDao extends HibernateDaoSupport implements CategoryDao {

	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#get(java.lang.Long)
	 */
	public Category get(Long pk) {
		// check if category id is defined
		Assert.notNull(pk);

		Category category = (Category) getHibernateTemplate().get(Category.class, pk);
		return category;
	}
	
	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#insert(cz.softinel.retra.category.Category)
	 */	
	public Category insert(Category category) {
		getHibernateTemplate().save(category);
		return category;
	}

	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#update(cz.softinel.retra.category.Category)
	 */
	public void update(Category category) {
		getHibernateTemplate().update(category);
	}

	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#delete(cz.softinel.retra.category.Category)
	 */
	public void delete(Category category) {
		//TODO: is there some better way, to do delete - without load?
		//must be at first loaded
		load(category);
		//than deleted
		getHibernateTemplate().delete(category);
	}

	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#findAll()
	 */
	public List<Category> selectAll() {
		@SuppressWarnings("unchecked")
		List<Category> result = getHibernateTemplate().loadAll(Category.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#selectAllNotDeleted()
	 */
	@SuppressWarnings("unchecked")
	public List<Category> selectAllNotDeleted() {

		Object[] states = new Object[] {Category.STATE_DELETED}; 

		Session session = getSession();
		Query query = session.getNamedQuery("Category.selectAllWithoutStates");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	
	/**
	 * @see cz.softinel.retra.category.dao.CategoryDao#load(cz.softinel.retra.category.Category)
	 */
	public void load(Category category) {
		// check if activty is defined and has defined pk and than load it
		Assert.notNull(category);
		Assert.notNull(category.getPk());
		getHibernateTemplate().load(category, category.getPk());
	}
}