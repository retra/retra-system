package cz.softinel.retra.icompany.dao;

import java.util.List;

import cz.softinel.retra.icompany.Icompany;

/**
 * Dao for worklog icompanies.
 *
 * @author Petr Sígl
 */
public interface IcompanyDao {

	/**
	 * Returns icompany according to primary key.
	 * 
	 * @param pk primary key of icompany
	 * @return
	 */
	public Icompany get(Long pk);

	/**
	 * Insert Icompany
	 * 
	 * @param icompany to insert
	 */
	public Icompany insert(Icompany icompany);

	/**
	 * Update icompany
	 * 
	 * @param icompany to update
	 */
	public void update(Icompany icompany);

	/**
	 * Delete icompany
	 * 
	 * @param icompany to delete
	 */
	public void delete(Icompany icompany);

	/**
	 * Returns all activities
	 * 
	 * @return all activities
	 */
	public List<Icompany> selectAll();

	/**
	 * Returns all activities, which are not deleted
	 * 
	 * @return all activities
	 */
	public List<Icompany> selectAllNotDeleted();

	/**
	 * Load informations about icompany (defined by pk)
	 * 
	 * @param icompany where load and where is pk set
	 */
	public void load(Icompany icompany);
}
