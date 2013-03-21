package cz.softinel.retra.security;

import cz.softinel.retra.employee.Employee;
import cz.softinel.sis.security.SecurityContext;

public interface MiraSecurityContext extends SecurityContext {

	public Employee getLoggedEmployee();
	public void setLoggedEmployee(Employee employee);
	
}
