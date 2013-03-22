/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.contactinfo;


/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ContactInfo {

	private Long pk;

	private String firstName;
	private String lastName;
	private String email;
	private String web;
	private String phone1;
	private String phone2;
	private String fax;
	private String jirauser;

	// Business fields ...

	public String getDisplayName() {
		return ContactInfoHelper.getDisplayName(this);
	}
	
	
	// Getter and Setters ...
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public Long getPk() {
		return pk;
	}
	public void setPk(Long pk) {
		this.pk = pk;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}

	/**
	 * @return the jirauser
	 */
	public String getJirauser() {
		return jirauser;
	}
	/**
	 * @param jirauser the jirauser to set
	 */
	public void setJirauser(String jirauser) {
		this.jirauser = jirauser;
	}

}
