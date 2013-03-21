package cz.softinel.retra.security.blo;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.security.MiraSecurityContext;
import cz.softinel.retra.security.MiraSecurityContextImpl;
import cz.softinel.sis.security.SecurityContext;
import cz.softinel.sis.security.web.SecurityWebFilter;
import cz.softinel.sis.security.web.WebSecurityLogicImpl;
import cz.softinel.sis.user.User;

public class MiraSecurityLogicImpl extends WebSecurityLogicImpl implements MiraSecurityLogic {

	public MiraSecurityLogicImpl() {
		SecurityWebFilter.securityLogic = this;
	}
	
	private EmployeeLogic employeeLogic;

	// Configuration methods ...

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}
	
	// TODO ...
	
	@Override
	public SecurityContext newSecurityContext() {
		return new MiraSecurityContextImpl();
	}
	
	public MiraSecurityContext getMiraSecurityContext() {
		return (MiraSecurityContext) getSecurityContext();
	}
	
	private User loadEmployee(User user){
		if(user == null){
			return null;
		}
		//TODO: Use get method instead load
		Employee employee = new Employee();
		employee.setPk(user.getPk());
		employeeLogic.loadAndLoadLazy(employee);
		// TODO: It is really necessary?
		employee.setUser(user);
		
		// TODO: Find better solution ... set employee is not good method ...
		MiraSecurityContext securityContext = getMiraSecurityContext();
		securityContext.setLoggedEmployee(employee);
		
		return user;
	}

	public User login(String loginName, String loginPassword) {
		User user = super.login(loginName, loginPassword);
		return loadEmployee(user);
	}

	public User login(String permanentPassword) {
		User user = super.login(permanentPassword);
		return loadEmployee(user);
	}

	public Employee getLoggedEmployee() {
		return getMiraSecurityContext().getLoggedEmployee();
	}
	
	public void reloadLoggedEmployee() {
		Employee loggedEmployee = getLoggedEmployee();
		employeeLogic.loadAndLoadLazy(loggedEmployee);
	}
	
}
