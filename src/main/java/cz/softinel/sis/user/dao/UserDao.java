/*
 * Created on 8.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.sis.user.dao;

import java.util.List;

import cz.softinel.sis.user.User;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface UserDao {

	public User get(Long pk);
	
	public List<User> selectAll();
	
	public void load(User user);

	public void insert(User user);
	public void update(User user);
	public void delete(User user);

}
