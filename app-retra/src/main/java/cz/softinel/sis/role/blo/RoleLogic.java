/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.role.blo;

import java.util.List;

import cz.softinel.sis.role.Role;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface RoleLogic {

	public Role get(Long pk);

	public List<Role> findAll();

	public void create(Role role);
	public void store(Role role);
	public void remove(Role role);

}
