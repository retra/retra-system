package cz.softinel.sis.login.blo;

import java.util.List;

import cz.softinel.sis.login.Login;

/**
 * 
 * @version $Revision: 1.5 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public interface LoginLogic {

	public Login get(Long pk);

	public List<Login> findAll();

	public Login create(Login login);
	public void store(Login login);
	public void remove(Login login);

	public Login checkLogin(String name, String password);
	public Login checkLogin(String permanentPassword);

	public void logout(Login login);
	
	public void changePassword(Login login, String oldPassword, String newPassword);

	public void createPermanentPassword(Login login);
}
