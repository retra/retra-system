package cz.softinel.sis.contactinfo.web;

public class ContactInfoForm {

	private String pk;

	private String firstName;
	private String lastName;
	private String email;
	private String web;
	private String phone1;
	private String phone2;
	private String fax;
	private String jirauser;
	
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
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
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
