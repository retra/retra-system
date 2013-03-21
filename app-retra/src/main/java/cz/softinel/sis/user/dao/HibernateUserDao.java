/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.user.dao;

import java.util.List;

import cz.softinel.sis.user.User;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;


/**
 * @author Radek Pinc
 *
 */
public class HibernateUserDao extends AbstractHibernateDao implements UserDao {

	public User get(Long pk) {
		return (User) getHibernateTemplate().get(User.class, pk);
	}

	@SuppressWarnings("unchecked")
	public List<User> selectAll() {
		return getHibernateTemplate().loadAll(User.class);
	}

	public void load(User user) {
		getHibernateTemplate().load(user, user.getPk());
	}

	public void insert(User user) {
		getHibernateTemplate().save(user);
	}

	public void update(User user) {
		getHibernateTemplate().update(user);
	}

	public void delete(User user) {
		// TODO: Is really need load before delete???
		load(user);
		getHibernateTemplate().delete(user);
	}

}
