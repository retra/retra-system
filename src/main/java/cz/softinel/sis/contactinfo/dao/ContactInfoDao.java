/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.contactinfo.dao;

import java.util.List;

import cz.softinel.sis.contactinfo.ContactInfo;

/**
 * @author Radek Pinc
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface ContactInfoDao {

	public ContactInfo get(Long pk);

	public List<ContactInfo> selectAll();

	public void load(ContactInfo contactInfo);

	public void insert(ContactInfo contactInfo);

	public void update(ContactInfo contactInfo);

	public void delete(ContactInfo contactInfo);

}
