package cz.softinel.retra.component.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import cz.softinel.retra.component.Component;

public class HibernateComponentDao extends HibernateDaoSupport implements ComponentDao {

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#get(java.lang.Long)
	 */
	public Component get(Long pk) {
		// check if component id is defined
		Assert.notNull(pk);

		Component component = (Component) getHibernateTemplate().get(Component.class, pk);
		return component;
	}

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#insert(cz.softinel.retra.component.Component)
	 */
	public Component insert(Component component) {
		getHibernateTemplate().save(component);
		return component;
	}

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#update(cz.softinel.retra.component.Component)
	 */
	public void update(Component component) {
		getHibernateTemplate().update(component);
	}

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#delete(cz.softinel.retra.component.Component)
	 */
	public void delete(Component component) {
		load(component);
		getHibernateTemplate().delete(component);
	}

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#findAll()
	 */
	public List<Component> selectAll() {
		@SuppressWarnings("unchecked")
		List<Component> result = getHibernateTemplate().loadAll(Component.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#selectAllNotDeleted()
	 */
	@SuppressWarnings("unchecked")
	public List<Component> selectAllNotDeleted() {
		Object[] states = new Object[] { Component.STATE_DELETED };

		Session session = getSession();
		Query query = session.getNamedQuery("Component.selectAllWithoutStates");
		query.setParameterList("states", states);

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

	/**
	 * @see cz.softinel.retra.component.dao.ComponentDao#load(cz.softinel.retra.component.Component)
	 */
	public void load(Component component) {
		Assert.notNull(component);
		Assert.notNull(component.getPk());
		getHibernateTemplate().load(component, component.getPk());
	}

	@SuppressWarnings("unchecked")
	public List<Component> selectComponentsWithCode(String code) {
		Session session = getSession();
		Query query = session.getNamedQuery("Component.selectComponentsWithCode");
		query.setParameter("code", code);

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

}