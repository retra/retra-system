/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.user;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.role.Role;
import cz.softinel.sis.security.Permission;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class User implements Serializable {

	private Long pk;
	
	private String uid;
	private String state;
	
	private ContactInfo contactInfo;
	private Role personalRole;
	private Login login;
	
	//counted permissions from all roles
	private Set<Permission> permissions = new HashSet<Permission>(); 
	
	// Business fields ...

	
	// Getter and Setters ...
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return Returns the contactInfo.
	 */
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	/**
	 * @param contactInfo The contactInfo to set.
	 */
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	/**
	 * @return Returns the personalRole.
	 */
	public Role getPersonalRole() {
		return personalRole;
	}
	/**
	 * @param personalRole The personalRole to set.
	 */
	public void setPersonalRole(Role personalRole) {
		this.personalRole = personalRole;
	}
	/**
	 * @return Returns the pk.
	 */
	public Long getPk() {
		return pk;
	}
	/**
	 * @param pk The pk to set.
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}

	public void addPermission(final Permission permission) {
		permissions.add(permission);
	}
	
	public Set<Permission> getPermissions() {
		return permissions;
	}

	public Set<Role> getRoles() {
		if (personalRole != null) {
			return personalRole.getRoles();
		}
		return new HashSet<Role>();
	}
	
}
