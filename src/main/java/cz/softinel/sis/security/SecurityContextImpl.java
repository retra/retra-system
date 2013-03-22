package cz.softinel.sis.security;

import cz.softinel.sis.user.User;

public abstract class SecurityContextImpl implements SecurityContext {

	private User loggedUser;
	
	public boolean isUserLoggedIn() {
		return loggedUser != null;
	}
	
	public User getLoggedUser() {
		return loggedUser;
	}

	public void login(User user) {
		// TODO radek: Check if is user already logged in ...
		loggedUser = user;
	}

	public void logout() {
		loggedUser = null;
	}
	
}
