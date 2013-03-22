package cz.softinel.sis.login.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.LoginException;
import cz.softinel.sis.login.LoginHelper;
import cz.softinel.sis.login.dao.LoginDao;
import cz.softinel.sis.login.ldap.LdapUserDetailsService;
import cz.softinel.uaf.messages.Message;

/**
 * 
 * @version $Revision: 1.7 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public class LoginLogicImpl extends AbstractLogicBean implements LoginLogic {

	private LoginDao loginDao;
	private LdapUserDetailsService ldapUserDetailsService;
	private boolean useLdapAuth = false;
	private boolean useLdapOnly = false;
	
	
	// Configuration methods ...
	
	public void setLdapUserDetailsService(LdapUserDetailsService ldapUserDetailsService) {
		this.ldapUserDetailsService = ldapUserDetailsService;
	}

	public boolean isUseLdapAuth() {
		return useLdapAuth;
	}

	public void setUseLdapAuth(boolean useLdapAuth) {
		this.useLdapAuth = useLdapAuth;
	}

	public boolean isUseLdapOnly() {
		return useLdapOnly;
	}

	public void setUseLdapOnly(boolean useLdapOnly) {
		this.useLdapOnly = useLdapOnly;
	}

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

	// Public business logic ...

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Login checkLogin(String name, String password) {
		if (!StringUtils.hasLength(password) || !StringUtils.hasLength(name)) {
			return null;
		}
		String hashedPassword = LoginHelper.hashPassword(password);

		Login login = null;

		//try normal login
		if (!useLdapOnly) {
			login = loginDao.getByName(name);

			if (login != null) {
				if (!hashedPassword.equals(login.getPassword())) {
					login = null;					
				}
			}
		}

		//try LDAP
		if (login == null && useLdapAuth) {
			login = loginDao.getByLdapLogin(name);
			boolean auth = ldapUserDetailsService.authenticateUser(name, password);
			if (login != null) {
				if (!auth) {
					login = null;
				}
			}
		}
		
		//return login
		return login;
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Login checkLogin(String permanentPassword) {
		if (permanentPassword == null) {
			return null;
		}
		return loginDao.getByPermanentPassword(permanentPassword);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Login create(Login login) {
		validateForCreate(login);
		return loginDao.insert(login);
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Login> findAll() {
		return loginDao.selectAll();
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Login get(Long pk) {
		return loginDao.get(pk);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(Login login) {
		loginDao.delete(login);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void store(Login login) {
		validateForUpdate(login);
		loginDao.update(login);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void logout(Login login) {
		loginDao.load(login);
		login.setPermanentPassword(null);
		loginDao.update(login);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void changePassword(Login login, String oldPassword, String newPassword) {
		if(!LoginHelper.isPasswordSecure(newPassword)){
			addError(new Message("employeeManagement.create.password.not.secure"));
			return;
		}
		loginDao.load(login);
		oldPassword = LoginHelper.hashPassword(oldPassword);
		newPassword = LoginHelper.hashPassword(newPassword);
		if (! login.getPassword().equals(oldPassword)) {
			addError(new Message("employeeManagement.changePassword.error.badPassword"));
			return;
		}
		login.setPassword(newPassword);
		loginDao.update(login);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void createPermanentPassword(Login login) {
		loginDao.load(login);
		login.setPermanentPassword(LoginHelper.generatePermanentPassword(login));
		loginDao.update(login);
	}

	// Validation methods ...
	
	protected void validateForCreate(Login login) {
		validate(login);
		if(loginDao.usernameExists(login.getName())){
			throw new LoginException("Username is already taken");
		}
	}

	protected void validateForUpdate(Login login) {
		validate(login);
		if(loginDao.usernameExists(login.getName())){
			throw new LoginException("Username is already taken");
		}
	}
	
	protected void validate(Login login) {
		if(!LoginHelper.isPasswordSecure(login.getPassword())){
			throw new LoginException("Password is not secure enough");
		}
	}

}
