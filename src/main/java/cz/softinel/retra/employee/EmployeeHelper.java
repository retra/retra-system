package cz.softinel.retra.employee;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.employee.web.EmployeeForm;
import cz.softinel.retra.icompany.Icompany;
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
		
		final Long icompanyPk = LongConvertor.getLongFromString(form.getIcompany());
		if (icompanyPk == null) {
			entity.setIcompany(null);
		} else {
			Icompany icompany = entity.getIcompany();
			if (icompany == null) {
				icompany = new Icompany();
			}
			entity.setIcompany(icompany);
			icompany.setPk(icompanyPk);
		}
		
		entity.setIgenerate(form.getIgenerate());
		entity.setProjects(form.getProjects());
	}

	public static void entityToForm(Employee entity, EmployeeForm form) {
		
		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));
		
		User user = entity.getUser();
		if (user != null) {
			UserHelper.entityToForm(user, form.getUser());
		}

		Icompany icompany = entity.getIcompany();
		if (icompany != null) {
			String icompanyPk = LongConvertor.convertToStringFromLong(entity.getIcompany().getPk());
			form.setIcompany(icompanyPk);
		}

		form.setIgenerate(entity.getIgenerate());
		form.setProjects(entity.getProjects());
	}
	
}
