package cz.softinel.retra.security;

import cz.softinel.retra.employee.Employee;
import cz.softinel.sis.security.SecurityContextImpl;

public class MiraSecurityContextImpl extends SecurityContextImpl implements MiraSecurityContext {

	private Employee loggedEmployee;

	public Employee getLoggedEmployee() {
		return loggedEmployee;
	}

	public void setLoggedEmployee(Employee employee) {
		loggedEmployee = employee;
	}

	@Override
	public String toString() {
		return super.toString() + "=>(MiraSecurityContextImpl: loggedEmployee="
			+ (loggedEmployee != null ? loggedEmployee.toString() : "")
			+ loggedEmployee + ")";
	}
	
}
