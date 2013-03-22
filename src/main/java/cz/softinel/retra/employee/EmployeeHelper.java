package cz.softinel.retra.employee;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.web.EmployeeForm;
import cz.softinel.sis.user.User;
import cz.softinel.sis.user.UserHelper;

public class EmployeeHelper {

	// TODO: It is good? Web form mapping on helper class???
	public static void formToEntity(EmployeeForm form, Employee entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		// Map user only if is required ...
		if (entity.getUser() != null) {
			UserHelper.formToEntity(form.getUser(), entity.getUser());
		}
		
		entity.setProjects(form.getProjects());
	}

	public static void entityToForm(Employee entity, EmployeeForm form) {
		
		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));
		
		User user = entity.getUser();
		if (user != null) {
			UserHelper.entityToForm(user, form.getUser());
		}
		
		form.setProjects(entity.getProjects());
	}
	
}
