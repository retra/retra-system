/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.login;

import java.io.Serializable;

import cz.softinel.sis.user.User;

/**
 * @author Radek Pinc
 *
 */
public class Login implements Serializable {

	private Long pk;
	private String name;
	/**
	 * New property for integrating with LDAP authentication. If LDAP authentication is set to true
	 * and the LDAP server configured, the users will be loaded not by the Login.name but the 
	 * Login.ldapLogin name.
	 * @author Erik Szalai
	 */
	private String ldapLogin;
	private String password;
	private String permanentPassword;
	private int state;
	
	private User user;
	
	// Business fields ...
	
	public boolean isActive() {
		return LoginHelper.isActive(this);
	}
	
	// Getter and Setters ...
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return The login name into the ldap system.
	 * @see Login#ldapLogin
	 */
	public String getLdapLogin() {
		return ldapLogin;
	}

	/**
	 * Set the login name into the LDAP system.
	 * @param ldapLogin
	 * @see Login#ldapLogin
	 */
	public void setLdapLogin(String ldapLogin) {
		this.ldapLogin = ldapLogin;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the pk.
	 */
	public Long getPk() {
		return pk;
	}
	/**
	 * @param pk The pk to set.
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}
	/**
	 * @return Returns the state.
	 */
	public int getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}
	public String getPermanentPassword() {
		return permanentPassword;
	}
	public void setPermanentPassword(String permanentPassword) {
		this.permanentPassword = permanentPassword;
	}
	
}
