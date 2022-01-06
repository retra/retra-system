/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.contactinfo.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.contactinfo.dao.ContactInfoDao;

/**
 * 
 * @version $Revision: 1.4 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public class ContactInfoLogicImpl extends AbstractLogicBean implements ContactInfoLogic {

	private ContactInfoDao contactInfoDao;

	// Configuration methods ...

	public void setContactInfoDao(ContactInfoDao contactInfoDao) {
		this.contactInfoDao = contactInfoDao;
	}

	// Public business logic ...

	@Transactional(propagation = Propagation.REQUIRED)
	public void create(ContactInfo contactInfo) {
		validateForCreate(contactInfo);
		contactInfoDao.insert(contactInfo);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<ContactInfo> findAll() {
		return contactInfoDao.selectAll();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public ContactInfo get(Long pk) {
		return contactInfoDao.get(pk);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(ContactInfo contactInfo) {
		contactInfoDao.delete(contactInfo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void store(ContactInfo contactInfo) {
		validateForUpdate(contactInfo);
		contactInfoDao.update(contactInfo);
	}

	// Validation methods ...

	protected void validateForCreate(ContactInfo contactInfo) {
		// TODO: Implement validations
		validate(contactInfo);
	}

	protected void validateForUpdate(ContactInfo contactInfo) {
		// TODO: Implement validations
		validate(contactInfo);
	}

	protected void validate(ContactInfo contactInfo) {
		// TODO: Implement validations
	}

}
