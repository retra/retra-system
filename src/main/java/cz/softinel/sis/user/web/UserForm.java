package cz.softinel.sis.user.web;

import cz.softinel.sis.contactinfo.web.ContactInfoForm;
import cz.softinel.sis.login.web.LoginForm;

public class UserForm {

	private String pk;

	private LoginForm login;
	private ContactInfoForm contactInfo;

	// Getters and setters ...
	
	public LoginForm getLogin() {
		if (login == null) {
			login = new LoginForm();
		}
		return login;
	}
	
	public ContactInfoForm getContactInfo() {
		if (contactInfo == null) {
			contactInfo = new ContactInfoForm();
		}
		return contactInfo;
	}



	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}
	
	
}
