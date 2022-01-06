package cz.softinel.sis.login.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.util.StringUtils;

public class LdapUserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(LdapUserDetailsService.class);

	private LdapTemplate ldapTemplate;
	private String dnBase;
	private String filterBase;

	public void setDnBase(String dnBase) {
		this.dnBase = dnBase;
	}

	public void setFilterBase(String filterBase) {
		this.filterBase = filterBase;
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public boolean authenticateUser(String username, String password) {
		// username and password are required
		if (!StringUtils.hasLength(password) || !StringUtils.hasLength(username)) {
			return false;
		}

		String filter = filterBase.replace("{0}", username);
		boolean result = false;
		try {
			result = ldapTemplate.authenticate(dnBase, filter, password);
		} catch (Exception e) {
			logger.warn("Problem with LDAP authenticate.", e);
		}
		return result;
	}

}
