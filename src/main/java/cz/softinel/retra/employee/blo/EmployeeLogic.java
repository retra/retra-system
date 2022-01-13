package cz.softinel.retra.employee.blo;

import java.util.Date;
import java.util.List;

import cz.softinel.retra.employee.Employee;

/**
 * @version $Revision: 1.7 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public interface EmployeeLogic {

	/**
	 * Returns all employees
	 * 
	 * @return
	 */
	List<Employee> getAllEmployees(final boolean onlyActive, final boolean onlyWorkLogging);

	/**
	 * Returns all employees
	 * 
	 * @return
	 */
	List<Employee> getAllEmployeesNotFull(final boolean onlyActive, final boolean onlyWorkLogging);

	/**
	 * Returns all employees for generating invoice
	 * 
	 * @return
	 */
	List<Employee> getAllEmployeesForGeneratingInvoice(final Date startDate, final Date finishDate);

	/**
	 * Returns all active employees id in given interval.
	 * 
	 * @return
	 */
	List<Employee> getAllActiveEmployeesWithWorklogIdInInterval(final Date startDate, final Date finishDate);
	
	/**
	 * Load emlpoyee instance fields
	 * 
	 * @param employee
	 */
	public void load(Employee employee);

	/**
	 * Load emlpoyee instance fields
	 * 
	 * @param employee
	 */
	public void loadAndLoadLazy(Employee employee);

	/**
	 * Get employee according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public Employee get(Long pk);

	/**
	 * Get employee according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public Employee getNotFull(Long pk);

	/** Create new employee */
	public Employee create(Employee employee);

	/**
	 * Stores given Employee
	 * 
	 * @param Employee
	 */
	void store(Employee employee);

}
