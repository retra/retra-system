package cz.softinel.retra.security.blo;

import cz.softinel.retra.employee.Employee;
import cz.softinel.sis.security.web.WebSecurityLogic;

public interface MiraSecurityLogic extends WebSecurityLogic {

	public Employee getLoggedEmployee();

	public void reloadLoggedEmployee();
}
