/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.contactinfo.blo;

import java.util.List;

import cz.softinel.sis.contactinfo.ContactInfo;

/**
 * @author Radek Pinc
 *
 */
public interface ContactInfoLogic {

	public ContactInfo get(Long pk);

	public List<ContactInfo> findAll();

	public void create(ContactInfo contactInfo);
	public void store(ContactInfo contactInfo);
	public void remove(ContactInfo contactInfo);


}
