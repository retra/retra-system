package cz.softinel.sis.contactinfo;

import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.sis.contactinfo.web.ContactInfoForm;

public class ContactInfoHelper {

	public static String getDisplayName(ContactInfo contactInfo) {
		StringBuffer sb = new StringBuffer();
//		sb.append(contactInfo.getFirstName());
		sb.append(contactInfo.getLastName());
		sb.append(" ");
//		sb.append(contactInfo.getLastName());
		sb.append(contactInfo.getFirstName());
		return sb.toString().trim();
	}

	// TODO: Find more automatic solution!!!
	// FIXME: This is work for skilled monkey!!!
	public static void formToEntity(ContactInfoForm form, ContactInfo entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		if (form.getFirstName() != null) {
			entity.setFirstName(form.getFirstName());
		}
		if (form.getLastName() != null) {
			entity.setLastName(form.getLastName());
		}
		if (form.getEmail() != null) {
			entity.setEmail(form.getEmail());
		}
		if (form.getWeb() != null) {
			entity.setWeb(form.getWeb());
		}
		if (form.getPhone1() != null) {
			entity.setPhone1(form.getPhone1());
		}
		if (form.getPhone2() != null) {
			entity.setPhone2(form.getPhone2());
		}
		if (form.getFax() != null) {
			entity.setFax(form.getFax());
		}
		if (form.getJirauser() != null) {
			entity.setJirauser(form.getJirauser());
		}

	}

	public static void entityToForm(ContactInfo entity, ContactInfoForm form) {
		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));
		form.setFirstName(entity.getFirstName());
		form.setLastName(entity.getLastName());
		form.setEmail(entity.getEmail());
		form.setWeb(entity.getWeb());
		form.setPhone1(entity.getPhone1());
		form.setPhone2(entity.getPhone2());
		form.setFax(entity.getFax());
		form.setJirauser(entity.getJirauser());
	}

}
