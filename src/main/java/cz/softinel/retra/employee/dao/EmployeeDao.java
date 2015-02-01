/*
 * Created on 9.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee.dao;

import java.util.List;

import cz.softinel.retra.employee.Employee;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface EmployeeDao {

	public Employee get(Long pk);
	
	public Employee insert(Employee employee);
	public void update(Employee employee);
	public void delete(Employee employee);
	
	public List<Employee> findAll();
	public List<Employee> findAllForGenerate();
	
	public void load(Employee employee);
	public void loadAndLoadLazy(Employee employee);

}
