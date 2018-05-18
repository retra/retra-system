package cz.softinel.sis.role;

import cz.softinel.sis.security.PermissionHelper;
import cz.softinel.sis.user.User;

public abstract class RoleHelper {

	public static final String PERM_GROUP_WORKLOG_ADMIN = "PermGroup:WorklogAdmin";

	/**
	 * TODO: static assign permissisons to role. NOT nice.
	 * 
	 * @param user
	 */
	public static final void prepareUserPermissions(User user) {
		
		if (user != null && user.getRoles() != null && !user.getRoles().isEmpty()) {

			//TODO: FUJ: static mapping, not i DB :-(
			for (Role role : user.getRoles()) {
				if (PERM_GROUP_WORKLOG_ADMIN.equals(role.getId())) {
					user.addPermission(PermissionHelper.PERMISSION_VIEW_ALL_WORKLOGS);
				}
			}
			
		}
		
	}
	
}
