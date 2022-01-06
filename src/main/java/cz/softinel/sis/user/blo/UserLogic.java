package cz.softinel.sis.user.blo;

import java.util.List;

import cz.softinel.sis.user.User;

/**
 * 
 * @version $Revision: 1.4 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public interface UserLogic {

	public User create(User user);

	public void store(User user);

	public void remove(User user);

	public List<User> findAll();

}
