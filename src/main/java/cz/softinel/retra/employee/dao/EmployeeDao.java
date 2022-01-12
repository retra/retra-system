/*
 * Created on 9.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee.dao;

import java.util.Date;
import java.util.List;

import cz.softinel.retra.employee.Employee;

/**
 * @author Radek Pinc
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface EmployeeDao {

	public Employee get(Long pk);

	public Employee insert(Employee employee);

	public void update(Employee employee);

	public void delete(Employee employee);

	public List<Employee> findAll(final boolean onlyActive, final boolean onlyWorkLogging);

	public List<Employee> findAllForGenerate(final Date startDate, final Date finishDate);

	public void load(Employee employee);

	public void loadAndLoadLazy(Employee employee);

}
