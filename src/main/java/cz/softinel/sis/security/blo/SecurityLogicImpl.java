package cz.softinel.sis.security.blo;

import cz.softinel.sis.log.aspect.DoNotLogArgs;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.blo.LoginLogic;
import cz.softinel.sis.role.Role;
import cz.softinel.sis.security.Permission;
import cz.softinel.sis.security.SecurityContext;
import cz.softinel.sis.user.User;

/**
 * 
 * @version $Revision: 1.8 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 *         <a href="mailto:radek.pinc@seznam.cz">radek.pinc@seznam.cz</a>
 */
public abstract class SecurityLogicImpl implements SecurityLogic {

	private LoginLogic loginLogic;

	// Configuration setters ...

	public void setLoginLogic(LoginLogic loginLogic) {
		this.loginLogic = loginLogic;
	}

	// Implementation for SecurityLogic ...

	public abstract SecurityContext getSecurityContext();

	public User getLoggedUser() {
		return getSecurityContext().getLoggedUser();
	}

	public boolean isUserLoggedIn() {
		return getSecurityContext().getLoggedUser() != null;
	}

	public void login(User user) {
		getSecurityContext().login(user);
	}

	public void createPermanentPassword(Login login) {
		loginLogic.createPermanentPassword(login);
	}

	public void logout() {
		SecurityContext securityContext = getSecurityContext();
		if (securityContext.isUserLoggedIn()) {
			Login login = securityContext.getLoggedUser().getLogin();
			loginLogic.logout(login);
			securityContext.logout();
		}
	}

	private User loginUser(String loginName, String loginPassword, boolean isPermanentPasswordLogin) {
		Login login = null;
		if (isPermanentPasswordLogin) {
			login = loginLogic.checkLogin(loginPassword);
		} else {
			login = loginLogic.checkLogin(loginName, loginPassword);
		}
		if (login == null) {
			return null;
		}
		User user = login.getUser();
		login(user);
		return user;
	}

	@DoNotLogArgs(argIndexes = {1})
	public User login(String loginName, String loginPassword) {
		return loginUser(loginName, loginPassword, false);
	}

	@DoNotLogArgs(argIndexes = {0})
	public User login(String permanentPassword) {
		return loginUser(null, permanentPassword, true);
	}

	public boolean hasRole(Role role) {
		// TODO radek: Implement it ...
		return true;
	}

	public boolean hasPermission(Permission permission) {
		User user = getSecurityContext().getLoggedUser();

		if (user.getPermissions().contains(permission)) {
			return true;
		}

		return false;
	}

}
