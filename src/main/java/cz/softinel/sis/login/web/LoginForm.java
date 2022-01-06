package cz.softinel.sis.login.web;

public class LoginForm {

	private String pk;

	private String name;
	private String ldapLogin;
	private String password;
	private String passwordConfirmation;
	private String passwordOriginal;

	// Getters and setters ...

	public String getName() {
		return name;
	}

	public String getLdapLogin() {
		return ldapLogin;
	}

	public void setLdapLogin(String ldapLogin) {
		this.ldapLogin = ldapLogin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getPasswordOriginal() {
		return passwordOriginal;
	}

	public void setPasswordOriginal(String passwordOriginal) {
		this.passwordOriginal = passwordOriginal;
	}

	@Override
	public String toString() {
		return "(LoginForm: pk=" + pk + ", name=" + name + ", ldapLogin=" + ldapLogin + ")";
	}

	
}
