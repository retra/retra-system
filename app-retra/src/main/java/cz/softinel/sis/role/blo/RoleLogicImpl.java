package cz.softinel.sis.role.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.sis.role.Role;
import cz.softinel.sis.role.dao.RoleDao;

/**
 * 
 * @version $Revision: 1.4 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public class RoleLogicImpl extends AbstractLogicBean implements RoleLogic {

	private RoleDao roleDao;

	// Configuration methods ...
	
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	// Public business logic ...
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Role get(Long pk) {
		return roleDao.get(pk);
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Role> findAll() {
		return roleDao.selectAll();
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void create(Role role) {
		validateForCreate(role);
		roleDao.insert(role);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void store(Role contactInfo) {
		validateForUpdate(contactInfo);
		roleDao.update(contactInfo);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public void remove(Role contactInfo) {
		roleDao.delete(contactInfo);
	}

	// Validation methods ...
	
	protected void validateForCreate(Role role) {
		// TODO: Implement validations
		validate(role);
	}

	protected void validateForUpdate(Role role) {
		// TODO: Implement validations
		validate(role);
	}
	
	protected void validate(Role role) {
		// TODO: Implement validations
	}

}
