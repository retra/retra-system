package cz.softinel.retra.project;

import java.util.ArrayList;
import java.util.List;

import cz.softinel.retra.category.Category;
import cz.softinel.retra.core.utils.convertor.BigDecimalConvertor;
import cz.softinel.retra.core.utils.convertor.LongConvertor;
import cz.softinel.retra.invoice.web.InvoiceStateSelectValue;
import cz.softinel.retra.project.web.ProjectForm;

public class ProjectHelper {

	public final static List<InvoiceStateSelectValue> PROJECT_STATES = new ArrayList<InvoiceStateSelectValue>();

	static {
		PROJECT_STATES.add(new InvoiceStateSelectValue(Project.STATE_ACTIVE));
		PROJECT_STATES.add(new InvoiceStateSelectValue(Project.STATE_CLOSED));
		PROJECT_STATES.add(new InvoiceStateSelectValue(Project.STATE_DELETED));
	}

	public static String getCodeAndName(Project project) {
		StringBuffer sb = new StringBuffer();
		sb.append(project.getCode());
		sb.append(" - ");
		sb.append(project.getName());
		return sb.toString().trim();
	}

	public static void formToEntity(ProjectForm form, Project entity) {
		if (form.getPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getPk());
			entity.setPk(pk);
		}
		entity.setName(form.getName());
		entity.setCode(form.getCode());

		final Long categoryPk = LongConvertor.getLongFromString(form.getCategory());
		if (categoryPk == null) {
			entity.setCategory(null);
		} else {
			Category category = entity.getCategory();
			if (category == null) {
				category = new Category();
			}
			entity.setCategory(category);
			category.setPk(categoryPk);
		}

		entity.setEmployees(form.getEmployees());
		entity.setComponents(form.getComponents());
		if (form.getParentPk() != null) {
			Long pk = LongConvertor.getLongFromString(form.getParentPk());
			if (pk.equals(Project.DUMMY_PROJECT_PK)) {
				entity.setParent(null);
			} else {
				Project parent = new Project();
				parent.setPk(pk);
				entity.setParent(parent);
			}
		} else {
			entity.setParent(null);
		}
		entity.setEstimation(BigDecimalConvertor.getBigDecimalFromString(form.getEstimation()));
		entity.setWorkEnabled(form.getWorkEnabled());
	}

	public static void entityToForm(Project entity, ProjectForm form) {

		form.setPk(LongConvertor.convertToStringFromLong(entity.getPk()));

		form.setName(entity.getName());
		form.setCode(entity.getCode());

		Category category = entity.getCategory();
		if (category != null) {
			String categoryPk = LongConvertor.convertToStringFromLong(entity.getCategory().getPk());
			form.setCategory(categoryPk);
		}

		if (entity.getParent() != null) {
			form.setParentPk(entity.getParent().getPk().toString());
		} else {
			form.setParentPk(null);
		}

		form.setEmployees(entity.getEmployees());
		form.setComponents(entity.getComponents());

		form.setWorkEnabled(entity.getWorkEnabled());
		form.setEstimation(BigDecimalConvertor.convertToStringFromBigDecimal(entity.getEstimation()));
	}

}
