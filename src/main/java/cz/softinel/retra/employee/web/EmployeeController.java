/*
 * Created on 7.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee.web;

import java.util.Comparator;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.employee.EmployeeNameComparator;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.uaf.spring.web.controller.CommonDispatchController;
import cz.softinel.uaf.spring.web.controller.Model;
import cz.softinel.uaf.spring.web.controller.RequestContext;

/**
 * @author Radek Pinc
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EmployeeController extends CommonDispatchController {

	private EmployeeLogic employeeLogic;
	/**
	 * Comparator added to correct the bug RTR-10. This will compare user names by the first name. Check
	 * the implementation {@link EmployeeNameComparator}.
	 */
	private Comparator<String> nameComparator;

	// Configuration setter methods ..
	
	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}
	
	/**
	 * Sets the employee name comparator.
	 * @param nameComparator
	 */
	public void setNameComparator(Comparator<String> nameComparator) {
		this.nameComparator = nameComparator;
	}
	
	public ModelAndView employeeManagement(Model model, RequestContext requestContext)
	{
		List<Employee> list = employeeLogic.getAllEmployees(false, false);
		model.set("list", list);
		model.set("nameComparator", nameComparator );
		return createModelAndView(model, getSuccessView());
	}	
	
	/**
	 * View given employee. 
	 * 
	 * @param model
	 * @param requestContext
	 * @return
	 */
	public ModelAndView employeeView(Model model, RequestContext requestContext)	{
		String strPk = requestContext.getParameter("pk");
		Long pk = LongConvertor.getLongFromString(strPk);
		//TODO: maybe assert will be better
		if (pk != null) {
			//TODO: must be check if user is deleting his worklog and not someone others - maybe in logic
			Employee employee = new Employee(); 
			employee.setPk(pk);
			employeeLogic.loadAndLoadLazy(employee);
			model.put("employee", employee);
		}
		return createModelAndView(model, getSuccessView());
	}
}
