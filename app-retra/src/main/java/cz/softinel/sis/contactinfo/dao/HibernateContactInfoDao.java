/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.contactinfo.dao;

import java.util.List;

import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

/**
 * @author Radek Pinc
 *
 */
public class HibernateContactInfoDao extends AbstractHibernateDao implements ContactInfoDao {

	public ContactInfo get(Long pk) {
		return (ContactInfo) getHibernateTemplate().get(ContactInfo.class, pk);
	}

	@SuppressWarnings("unchecked")
	public List<ContactInfo> selectAll() {
		return getHibernateTemplate().loadAll(ContactInfo.class);
	}

	public void load(ContactInfo contactInfo) {
		getHibernateTemplate().load(contactInfo, contactInfo.getPk());
	}

	public void insert(ContactInfo contactInfo) {
		getHibernateTemplate().save(contactInfo);
	}

	public void update(ContactInfo contactInfo) {
		getHibernateTemplate().update(contactInfo);
	}

	public void delete(ContactInfo contactInfo) {
		// TODO: Is really need load before delete???
		load(contactInfo);
		getHibernateTemplate().delete(contactInfo);
	}

}
