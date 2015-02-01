package cz.softinel.retra.employee.web;

import java.util.List;

import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.icompany.Icompany;
import cz.softinel.retra.icompany.blo.IcompanyLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.uaf.spring.web.controller.Model;

public abstract class AbstractEmployeeFormController extends FormController {
	
	private IcompanyLogic icompanyLogic;
	private EmployeeLogic employeeLogic;
	
	/**
	 * @return the icompanyLogic
	 */
	public IcompanyLogic getIcompanyLogic() {
		return icompanyLogic;
	}

	/**
	 * @param icompanyLogic the icompanyLogic to set
	 */
	public void setIcompanyLogic(IcompanyLogic icompanyLogic) {
		this.icompanyLogic = icompanyLogic;
	}

	/**
	 * @return the employeeLogic
	 */
	public EmployeeLogic getEmployeeLogic() {
		return employeeLogic;
	}

	/**
	 * @param employeeLogic the employeeLogic to set
	 */
	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	protected void prepareIcompanies(Model model) {
		prepareIcompanies(model, false);
	}
	
	protected void prepareIcompanies(Model model, boolean showAll) {
		List<Icompany> icompanies = null;
		if (showAll) {
			icompanies = icompanyLogic.findAllIcompanies();
		} else {
			icompanies = icompanyLogic.findAllNotDeletedIcompanies();			
		}
		prepareIcompanies(model, icompanies);
	}

	protected void prepareIcompanies(Model model, List icompanies) {
		model.put("icompanies", icompanies);
	}
}
