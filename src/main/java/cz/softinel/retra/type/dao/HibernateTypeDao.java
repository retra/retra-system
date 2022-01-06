package cz.softinel.retra.type.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import cz.softinel.retra.type.Type;

public class HibernateTypeDao extends HibernateDaoSupport implements TypeDao {

	/**
	 * @see cz.softinel.retra.type.dao.TypeDao#get(java.lang.Long)
	 */
	public Type get(Long pk) {
		// check if type id is defined
		Assert.notNull(pk);

		return (Type) getHibernateTemplate().get(Type.class, pk);
	}

	/**
	 * @see cz.softinel.retra.type.dao.TypeDao#insert(cz.softinel.retra.type.Type)
	 */
	public Type insert(Type type) {
		getHibernateTemplate().save(type);
		return type;
	}

	/**
	 * @see cz.softinel.retra.type.dao.TypeDao#update(cz.softinel.retra.type.Type)
	 */
	public void update(Type type) {
		getHibernateTemplate().update(type);
	}

	/**
	 * @see cz.softinel.retra.type.dao.TypeDao#delete(cz.softinel.retra.type.Type)
	 */
	public void delete(Type type) {
		// TODO: is there some better way, to do delete - without load?
		// must be at first loaded
		load(type);
		// than deleted
		getHibernateTemplate().delete(type);
	}

	/**
	 * @see cz.softinel.retra.type.dao.TypeDao#findAll()
	 */
	public List<Type> selectAll() {
		@SuppressWarnings("unchecked")
		List<Type> result = getHibernateTemplate().loadAll(Type.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.type.dao.TypeDao#load(cz.softinel.retra.type.Type)
	 */
	public void load(Type type) {
		// check if type is defined and has defined pk and than load it
		Assert.notNull(type);
		Assert.notNull(type.getPk());
		getHibernateTemplate().load(type, type.getPk());
	}
}