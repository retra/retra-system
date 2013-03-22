package cz.softinel.retra.worklog.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.Worklog;

/**
 * Parser for tab-separated data format from csv file.
 * 
 * @author Milan Čečrdle
 */
public class CsvParser extends LineBasedParser {

	private static final int COUNT_TOKENS = 10;
	private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String SPLIT_STRING=",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
	private static final String PROJECT_MAPPING_COOKIE = "mira.worklog.csv.mapping.project";
	private static final String ACTIVITY_MAPPING_COOKIE = "mira.worklog.csv.mapping.activity";

	private SimpleDateFormat dataTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);

	public CsvParser(final String encoding) {
		super(encoding);
	}

	public Worklog parseWorklogItem(String line, Map<String, ProjectInvoiceHolder> projectMapping, Map<String, Activity> activityMapping) {
		try {
			String[] tokens = line.split(SPLIT_STRING);
			if (tokens.length < COUNT_TOKENS) {
				throw new ImportParsingException("Insufient count of tokens on line expected " + COUNT_TOKENS + ".");
			}
			Worklog worklog = new Worklog();
			// Date,Project Code,Component,Activity,Invoice,Employee,Description,From,To,Hours
			// 02.01.2013,SW-175/11,,IT-PRG,,Čečrdle Milan,PTL-1537,08:30,11:30,"3,0"
			ProjectInvoiceHolder project = findProjectInMap(projectMapping,removeQuotes(tokens[1]));
			Activity activity = findActivityInMap(activityMapping, tokens[3]);
			worklog.setActivity(activity);
			if (project!=null) {
				if (project.getProject()==null) {
					return null;
				}
				worklog.setProject(project.getProject());
				worklog.setInvoice(project.getInvoice());
			} else {
				return null;
			}
			worklog.setDescription(removeQuotes(tokens[6]));
			Date startDateTime = dataTimeFormatter.parse(tokens[0] + " " + tokens[7]);
			Date endDateTime = dataTimeFormatter.parse(tokens[0] + " " + tokens[8]);
			worklog.setWorkFrom(startDateTime);
			worklog.setWorkTo(endDateTime);
			return worklog;
		} catch (Exception exc) {
			System.out.println(exc);
			throw new ImportParsingException(exc.getMessage());
		}
	}

	private ProjectInvoiceHolder findProjectInMap(final Map<String, ProjectInvoiceHolder> projectMap, final String category) {
		ProjectInvoiceHolder project = projectMap.get(category);
		if (project == null) {
			project = projectMap.get(""); // Default project mapping
		}
		if (project == null) {
			throw new IllegalArgumentException("Unable to find internal project that should be mapped to worklog items");
		}
		return project;
	}

	private Activity findActivityInMap(Map<String, Activity> activityMap, String codeActivity) {
		return activityMap.get(codeActivity);
	}

	public boolean canExtractExternalActivity() {
		return true;
	}

	public ExternalActivity parseExternalActivity(String line) {
		String[] tokens = line.split(SPLIT_STRING);
		return new ExternalActivity(tokens[3], tokens[3]);
	}
	
	/**
	 * @see cz.softinel.retra.worklog.web.ImportDataParser#parseExternalProject(java.lang.String)
	 */
	public ExternalProject parseExternalProject(String line) {
		String[] tokens = line.split(SPLIT_STRING);
		return new ExternalProject(tokens[1], tokens[1]);
	}
	
	private String removeQuotes(String str) {
		if(str == null) {
			return str;
		}
		String clean = str.trim();
		if(clean.startsWith("\"")) {
			clean = clean.substring(1);
		}
		if(clean.endsWith("\"")) {
			clean = clean.substring(0, clean.length()-1);
		}
		return clean;
	}	

	public boolean canExtractExternalProject() {
		return true;
	}

	public boolean storesDataToCookies() {
		return true;
	}

	public String getActivityMappingCookieName() {
		return ACTIVITY_MAPPING_COOKIE;
	}

	public String getProjectMappingCookieName() {
		return PROJECT_MAPPING_COOKIE;
	}

	public boolean skipFirstLine() {
		return false;
	}

	public boolean canSpecifyImportRules() {
		return false;
	}

}
