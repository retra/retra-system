package cz.softinel.retra.worklog.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.worklog.Worklog;

/**
 * Parser for worklog import data. Parser expects line based worklog data
 * format.
 *
 * @author Pavel Mueller
 */
public interface ImportDataParser {

	/**
	 * Initialize parsed for a given data.
	 * 
	 * @param data imported data
	 * @param data
	 * @throws IOException
	 */
	public void init(byte[] data) throws IOException;

	/**
	 * Parses one line and extracts information about project.
	 * 
	 * @return item's project
	 */
	public Set<ExternalProject> parseExternalProjects();

	/**
	 * Parses one line and extracts information about activity.
	 * 
	 * @param line worklog item line
	 * @return item's activity
	 * @throws IOException
	 */
	public Set<ExternalActivity> parseExternalActivitys();

	/**
	 * Parses one line and creates whole worklog item.
	 * 
	 * @param line            worklog item line
	 * @param projectMapping  mapping between imported projects and Mira projects
	 * @param activityMapping mapping between imported activities and Mira
	 *                        activities
	 * @return worklog item, <code>null</code> if item cannot be mapped
	 */
	public List<Worklog> parseWorklogItems(Map<String, ProjectInvoiceHolder> projectMapping,
			Map<String, Activity> activityMapping);

	/**
	 * Returns whether current parser is able to extract external projects from
	 * import file. If not then user will have to choose one of projects instead of
	 * mapping projects to external projects.
	 * 
	 * @return
	 */
	public boolean canExtractExternalProject();

	/**
	 * Returns whether current parser implementation is able to extract external
	 * activities from import file. If not then user will have to choose one of
	 * activities instead of mapping activities to external activities.
	 * 
	 * @return
	 */
	public boolean canExtractExternalActivity();

	public boolean canSpecifyImportRules();

	/**
	 * Returns whether parser is able to remember selected values to cookies
	 * (usually mapping preferences)
	 * 
	 * @return
	 */
	public boolean storesDataToCookies();

	/**
	 * Returns a name of cookie in which project mappings should be stored and read
	 * from
	 * 
	 * @return
	 */
	public String getProjectMappingCookieName();

	/**
	 * Returns a name of cookie in which activity mappings should be stored and read
	 * from
	 * 
	 * @return
	 */
	public String getActivityMappingCookieName();

	public int getNumberOfLines();

	public int getParseErrors();

}