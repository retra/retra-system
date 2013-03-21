package cz.softinel.sis.user;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.contactinfo.ContactInfoHelper;
import cz.softinel.sis.login.Login;
import cz.softinel.sis.login.LoginHelper;
import cz.softinel.sis.user.web.UserForm;

public class UserHelper {

	public static void formToEntity(UserForm form, User entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		// Map user only if is required ...
		if (entity.getLogin() != null) {
			LoginHelper.formToEntity(form.getLogin(), entity.getLogin());
		}
		if (entity.getContactInfo() != null) {
			ContactInfoHelper.formToEntity(form.getContactInfo(), entity.getContactInfo());
		}
	}

	public static void entityToForm(User entity, UserForm form) {
		
		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));
		
		Login login = entity.getLogin();
		if (login != null) {
			LoginHelper.entityToForm(login, form.getLogin());
		}
		
		ContactInfo contactInfo = entity.getContactInfo();
		if (contactInfo != null) {
			ContactInfoHelper.entityToForm(contactInfo, form.getContactInfo());
		}
		
	}

	
}
