package cz.softinel.retra.employee.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.dao.EmployeeDao;
import cz.softinel.sis.user.blo.UserLogic;

/**
 * @version $Revision: 1.10 $ $Date: 2007-02-23 12:16:27 $
 * @author Radek Pinc
 */
public class EmployeeLogicImpl extends AbstractLogicBean implements EmployeeLogic {

	private EmployeeDao employeeDao;
	private EmployeeDao employeeJdbcDao;
	
	private UserLogic userLogic;
	
	// Configuration setters ...
	
	public void setEmployeeDao(EmployeeDao employeeDao) {
		this.employeeDao = employeeDao;
	}
	
	public void setEmployeeJdbcDao(EmployeeDao employeeJdbcDao) {
		this.employeeJdbcDao = employeeJdbcDao;
	}

	public void setUserLogic(UserLogic userLogic) {
		this.userLogic = userLogic;
	}

	// Logic implementation ...
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Employee> getAllEmployees() {
		return employeeDao.findAll();
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public List<Employee> getAllEmployeesNotFull() {
		return employeeJdbcDao.findAll();
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public void load(Employee employee) {
		employeeDao.load(employee);
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public void loadAndLoadLazy(Employee employee) {
		employeeDao.loadAndLoadLazy(employee);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Employee get(Long pk) {
		Employee employee = employeeDao.get(pk);
		return employee;
	}

	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	public Employee getNotFull(Long pk) {
		Employee employee = employeeJdbcDao.get(pk);
		return employee;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Employee create(Employee employee) {
		// Business validation ...
		// TODO: Duplicit name ...
		// TODO: Safe password ...
		userLogic.create(employee.getUser());
		// TODO: WHY ???
		employee.setPk(employee.getUser().getPk());
		return employeeDao.insert(employee);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void store(Employee employee) {
		employeeDao.update(employee);
	}
}
