package cz.softinel.retra.icompany.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import cz.softinel.retra.icompany.Icompany;

/**
 * This class is hibernate dependent implementation of IcompanyDao
 *
 * @author Petr Sígl
 */
public class HibernateIcompanyDao extends HibernateDaoSupport implements IcompanyDao {

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#get(java.lang.Long)
	 */
	public Icompany get(Long pk) {
		// check if icompany id is defined
		Assert.notNull(pk);

		Icompany icompany = (Icompany) getHibernateTemplate().get(Icompany.class, pk);
		return icompany;
	}

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#insert(cz.softinel.retra.icompany.Icompany)
	 */
	public Icompany insert(Icompany icompany) {
		getHibernateTemplate().save(icompany);
		return icompany;
	}

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#update(cz.softinel.retra.icompany.Icompany)
	 */
	public void update(Icompany icompany) {
		getHibernateTemplate().update(icompany);
	}

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#delete(cz.softinel.retra.icompany.Icompany)
	 */
	public void delete(Icompany icompany) {
		// TODO: is there some better way, to do delete - without load?
		// must be at first loaded
		load(icompany);
		// than deleted
		getHibernateTemplate().delete(icompany);
	}

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#findAll()
	 */
	public List<Icompany> selectAll() {
		@SuppressWarnings("unchecked")
		List<Icompany> result = getHibernateTemplate().loadAll(Icompany.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#selectAllNotDeleted()
	 */
	@SuppressWarnings("unchecked")
	public List<Icompany> selectAllNotDeleted() {

		Object[] states = new Object[] { Icompany.STATE_DELETED };

		Session session = getSession();
		Query query = session.getNamedQuery("Icompany.selectAllWithoutStates");
		query.setParameterList("states", states);

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

	/**
	 * @see cz.softinel.retra.icompany.dao.IcompanyDao#load(cz.softinel.retra.icompany.Icompany)
	 */
	public void load(Icompany icompany) {
		// check if activty is defined and has defined pk and than load it
		Assert.notNull(icompany);
		Assert.notNull(icompany.getPk());
		getHibernateTemplate().load(icompany, icompany.getPk());
	}
}