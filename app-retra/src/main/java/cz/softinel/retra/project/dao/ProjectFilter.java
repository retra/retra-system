package cz.softinel.retra.project.dao;

import java.util.ArrayList;
import java.util.List;

import cz.softinel.uaf.filter.EntityFilter;

public class ProjectFilter extends EntityFilter {

	public static final String PROJECT_FILTER_NAME = "projectFilterName";
	public static final String PROJECT_FILTER_CODE = "projectFilterCode";
	public static final String PROJECT_FILTER_STATE = "projectFilterState";
	public static final String PROJECT_FILTER_EMPLOYEE = "projectFilterEmployee";
	
	private static List<String> fieldNames = new ArrayList<String>();

	static {
		fieldNames.add(PROJECT_FILTER_NAME);
		fieldNames.add(PROJECT_FILTER_CODE);
		fieldNames.add(PROJECT_FILTER_STATE);
		fieldNames.add(PROJECT_FILTER_EMPLOYEE);
	}
	
	public ProjectFilter() {
		super(ProjectFilter.fieldNames);
	}
}
