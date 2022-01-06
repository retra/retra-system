/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.role.dao;

import java.util.List;

import cz.softinel.sis.role.Role;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

/**
 * @author Radek Pinc
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateRoleDao extends AbstractHibernateDao implements RoleDao {

	public Role get(Long pk) {
		return (Role) getHibernateTemplate().get(Role.class, pk);
	}

	@SuppressWarnings("unchecked")
	public List<Role> selectAll() {
		return getHibernateTemplate().loadAll(Role.class);
	}

	public void load(Role role) {
		getHibernateTemplate().load(role, role.getPk());
	}

	public void insert(Role role) {
		getHibernateTemplate().save(role);
	}

	public void update(Role role) {
		getHibernateTemplate().update(role);
	}

	public void delete(Role role) {
		// TODO: Is really need load before delete???
		load(role);
		getHibernateTemplate().delete(role);
	}

}
