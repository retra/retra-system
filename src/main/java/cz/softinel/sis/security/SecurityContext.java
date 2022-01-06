package cz.softinel.sis.security;

import java.io.Serializable;

import cz.softinel.sis.user.User;

/**
 * 
 * @author Radek Pinc
 *
 */
public interface SecurityContext extends Serializable {

	/** Check if user is logged in */
	public boolean isUserLoggedIn();

	/** Get logged user, null if user is not logged in */
	public User getLoggedUser();

	/** Login inser into security context */
	public void login(User user);

	/** Logout user */
	public void logout();

}
