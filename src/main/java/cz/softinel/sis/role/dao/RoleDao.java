/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.role.dao;

import java.util.List;

import cz.softinel.sis.role.Role;

/**
 * @author Radek Pinc
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface RoleDao {

	public Role get(Long pk);

	public List<Role> selectAll();

	public void load(Role role);

	public void insert(Role role);

	public void update(Role role);

	public void delete(Role role);

}
