package cz.softinel.retra.component;

import cz.softinel.retra.component.web.ComponentForm;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.project.Project;

/**
 * This class contains "helper" methods, static variables for Components.
 * 
 * @author Zoltan Vadasz
 */
public class ComponentHelper {

	public static String getCodeAndName(Component component) {
		if (component.getCode().length() == 0) {
			return component.getName();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append("<!--");
			sb.append(component.getProject().getPk());
			sb.append("-->");
			sb.append(component.getCode());
			sb.append(" - ");
			sb.append(component.getName());
			return sb.toString().trim();
		}
	}

	public static void formToEntity(ComponentForm form, Component entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		entity.setName(form.getName());
		entity.setCode(form.getCode());

		if (form.getProjectPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getProjectPk());
			Project project = new Project();
			project.setPk(pk);
			entity.setProject(project);
		} else {
			entity.setProject(null);
		}
	}

	public static void entityToForm(Component entity, ComponentForm form) {
		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));
		form.setName(entity.getName());
		form.setCode(entity.getCode());
		form.setProjectPk(entity.getProject().getPk().toString());
	}

}
