/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.login.dao;

import java.util.List;

import cz.softinel.sis.login.Login;

/**
 * @author Radek Pinc
 *
 */
public interface LoginDao {

	public Login get(Long pk);

	public Login getByName(String name);

	/**
	 * Return a Login object, that has the specified ldapLogin property (ldap login
	 * name).
	 * 
	 * @param ldapLogin The name of the user (Login) in LDAP.
	 * @return A Login or null.
	 */
	public Login getByLdapLogin(String ldapLogin);

	public Login getByPermanentPassword(String permanentPassword);

	public List<Login> selectAll();

	public void load(Login login);

	public Login insert(Login login);

	public void update(Login login);

	public void delete(Login login);

	public boolean usernameExists(String username);

	/**
	 * Check for a Login which has the given LDAP login name.
	 * 
	 * @param ldapLogin
	 * @return
	 */
	public boolean ldapLoginExists(String ldapLogin);

}
