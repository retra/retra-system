/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.login.dao;

import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import cz.softinel.sis.login.Login;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

/**
 * @author Radek Pinc
 *
 */
public class HibernateLoginDao extends AbstractHibernateDao implements LoginDao {

	public Login get(Long pk) {
		return (Login) getHibernateTemplate().get(Login.class, pk);
	}

	public Login getByName(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		List list = getHibernateTemplate().findByNamedQueryAndNamedParam("Login.getByName", "name", name);
		return (Login) getExactlyOne(list);
	}

	public Login getByLdapLogin(String ldapLogin) {
		if (!StringUtils.hasText(ldapLogin)) {
			return null;
		}
		List list = getHibernateTemplate().findByNamedQueryAndNamedParam("Login.getByLdapLogin", "ldapLogin",
				ldapLogin);
		return (Login) getExactlyOne(list);
	}

	public Login getByPermanentPassword(String permanentPassword) {
		List list = getHibernateTemplate().findByNamedQueryAndNamedParam("Login.getByPermanentPassword",
				"permanentPassword", permanentPassword);
		return (Login) getExactlyOne(list);
	}

	public Login insert(Login login) {
		Assert.notNull(login);
		getHibernateTemplate().save(login);
		return login;
	}

	public void update(Login login) {
		Assert.notNull(login);
		getHibernateTemplate().update(login);
	}

	public void delete(Login login) {
		// TODO: is there some better way, to do delete - without load?
		// must be at first loaded
		load(login);
		// than deleted
		getHibernateTemplate().delete(login);
	}

	@SuppressWarnings("unchecked")
	public List<Login> selectAll() {
		return getHibernateTemplate().loadAll(Login.class);
	}

	public void load(Login login) {
		// check if worklog is defined and has defined pk and than load it
		Assert.notNull(login);
		Assert.notNull(login.getPk());
		getHibernateTemplate().load(login, login.getPk());
	}

	public boolean usernameExists(String username) {
		if (!StringUtils.hasText(username)) {
			return false;
		}
		List users = getHibernateTemplate().findByNamedQueryAndNamedParam("checkUsernameExists", "username", username);
		return users != null && !users.isEmpty();
	}

	public boolean ldapLoginExists(String ldapLogin) {
		if (!StringUtils.hasText(ldapLogin)) {
			return false;
		}
		List users = getHibernateTemplate().findByNamedQueryAndNamedParam("checkLdapLoginExists", "ldapLogin",
				ldapLogin);
		return users != null && !users.isEmpty();
	}

}
