package com.opensymphony.user.provider.ldap;

import java.util.Properties;
import com.opensymphony.user.provider.ldap.LDAPCredentialsProvider;

public class MasterPasswordLDAPCredentialsProvider extends LDAPCredentialsProvider {

	private static final long serialVersionUID = 1L;
	private String masterPassword;

	/* (non-Javadoc)
	 * @see com.opensymphony.user.provider.ldap.LDAPCredentialsProvider#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(String name, String password) {
		if (masterPassword == null) {
			throw new RuntimeException("MasterPassword is not configured!");
		} else if (password.equals(masterPassword)) {
			return true;
		}
		return super.authenticate(name, password);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.user.provider.ldap.LDAPCredentialsProvider#init(java.util.Properties)
	 */
	@Override
	public boolean init(Properties properties) {
        masterPassword = properties.getProperty("masterPassword");
		return super.init(properties);
	}

}
