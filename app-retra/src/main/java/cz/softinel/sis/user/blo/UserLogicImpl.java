package cz.softinel.sis.user.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.contactinfo.blo.ContactInfoLogic;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.LoginHelper;
import cz.softinel.sis.login.blo.LoginLogic;
import cz.softinel.sis.role.Role;
import cz.softinel.sis.role.blo.RoleLogic;
import cz.softinel.sis.user.User;
import cz.softinel.sis.user.dao.UserDao;

/**
 * 
 * @version $Revision: 1.8 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public class UserLogicImpl extends AbstractLogicBean implements UserLogic {

	private UserDao userDao;

	private LoginLogic loginLogic;
	private ContactInfoLogic contactInfoLogic;
	private RoleLogic roleLogic;

	// Configuration methods ...
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setLoginLogic(LoginLogic loginLogic) {
		this.loginLogic = loginLogic;
	}

	public void setContactInfoLogic(ContactInfoLogic contactInfoLogic) {
		this.contactInfoLogic = contactInfoLogic;
	}

	public void setRoleLogic(RoleLogic roleLogic) {
		this.roleLogic = roleLogic;
	}

	// Public business logic ...
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<User> findAll() {
		return userDao.selectAll();
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public User create(User user) {
		// Create contact info ...
		ContactInfo contactInfo = user.getContactInfo();
		contactInfoLogic.create(contactInfo);
		// Create personal role ...
		Role role = new Role();
		role.setId("personal-role-" + user.getLogin().getName());
		role.setName("Personal role for " + contactInfo.getDisplayName());
		roleLogic.create(role);
		// Create user ...
		user.setPersonalRole(role);
		userDao.insert(user);
		// Create login ...
		Login login = user.getLogin();
		login.setUser(user);
		login.setState(LoginHelper.STATE_ACTIVE);
		loginLogic.create(login);
		// Return created entity
		return user;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void store(User user) {
		userDao.update(user);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(User user) {
		userDao.delete(user);
	}

}
