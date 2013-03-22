package cz.softinel.retra.project.web;

import java.util.List;

import cz.softinel.retra.category.Category;
import cz.softinel.retra.category.blo.CategoryLogic;
import cz.softinel.retra.employee.blo.EmployeeLogic;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.project.blo.ProjectLogic;
import cz.softinel.retra.spring.web.FormController;
import cz.softinel.uaf.spring.web.controller.Model;

public abstract class AbstractProjectFormController extends FormController {
	
	private ProjectLogic projectLogic;
	private CategoryLogic categoryLogic;
	private EmployeeLogic employeeLogic;
	
	/**
	 * @return the projectLogic
	 */
	public ProjectLogic getProjectLogic() {
		return projectLogic;
	}

	/**
	 * @param projectLogic the projectLogic to set
	 */
	public void setProjectLogic(ProjectLogic projectLogic) {
		this.projectLogic = projectLogic;
	}

	/**
	 * @return the categoryLogic
	 */
	public CategoryLogic getCategoryLogic() {
		return categoryLogic;
	}

	/**
	 * @param categoryLogic the categoryLogic to set
	 */
	public void setCategoryLogic(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
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

	protected void prepareProjects(Model model) {
		List<Project> projects = null;
		projects = getProjectLogic().findAllParentProjects();	
		//create dummy project
		Project emptyProject = new Project();
		emptyProject.setCode("");
		emptyProject.setName("");
		emptyProject.setPk(Project.DUMMY_PROJECT_PK);		
		projects.add(emptyProject);
		//put projects into the model
		model.put("projects", projects);
	}	
	
	protected void prepareCategories(Model model) {
		prepareCategories(model, false);
	}
	
	protected void prepareCategories(Model model, boolean showAll) {
		List<Category> categories = null;
		if (showAll) {
			categories = categoryLogic.findAllCategories();
		} else {
			categories = categoryLogic.findAllNotDeletedCategories();			
		}
		prepareCategories(model, categories);
	}

	protected void prepareCategories(Model model, List categories) {
		model.put("categories", categories);
	}
}
