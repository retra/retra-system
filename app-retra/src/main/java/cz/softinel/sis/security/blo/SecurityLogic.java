package cz.softinel.sis.security.blo;

import cz.softinel.sis.login.Login;
import cz.softinel.sis.role.Role;
import cz.softinel.sis.security.Permission;
import cz.softinel.sis.security.SecurityContext;
import cz.softinel.sis.user.User;

/**
 * 
 * @author Radek Pinc
 *
 */
public interface SecurityLogic {

	// Context methods ...
	
	public SecurityContext getSecurityContext();
	public SecurityContext newSecurityContext();
	
	// Wraped Security Context class methods ...
	
	public boolean isUserLoggedIn();
	public User getLoggedUser();
	public void login(User user);
	public void logout();

	// Hi-Logic methods ...
	
	public User login(String loginName, String loginPassword);
	public User login(String permanentPassword);
	public void createPermanentPassword(Login login);
	public boolean hasRole(Role role);
	public boolean hasPermission(Permission permission);
	
}
