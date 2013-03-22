package cz.softinel.retra.type.blo;

import java.util.List;

import cz.softinel.retra.type.Type;

/**
 * this class represents all logic for type.
 *
 * @version $Revision: 1.1 $ $Date: 2007-01-29 07:11:42 $
 * @author Petr Sígl
 */
public interface TypeLogic {
	
	/**
	 * Find all type items.
	 * 
	 * @return
	 */
	public List<Type> findAllTypes();	
}
