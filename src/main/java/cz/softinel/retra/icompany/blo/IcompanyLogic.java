package cz.softinel.retra.icompany.blo;

import java.util.List;

import cz.softinel.retra.icompany.Icompany;

/**
 * This class represents all logic for icompanies.
 *
 * @author Petr Sígl
 */
public interface IcompanyLogic {

	/**
	 * Find all icompany items.
	 * 
	 * @return
	 */
	public List<Icompany> findAllIcompanies();

	/**
	 * Find all icompany items, which are not deleted.
	 * 
	 * @return
	 */
	public List<Icompany> findAllNotDeletedIcompanies();
}
