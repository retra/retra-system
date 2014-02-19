package cz.softinel.retra.worklog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;
import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.web.LineBasedParser.LineProcessor;

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
	List<String[]> splitLines;
	public CsvParser(final String encoding) {
		super(encoding);
	}
	
	public void init(byte[] data) throws IOException {
		final Reader reader = getDataReader(data);
		splitLines = splitAllLines(reader);
	}
	
	public List<Worklog> parseWorklogItems(final Map<String, ProjectInvoiceHolder> projectMapping, final Map<String, Activity> activityMapping) {		
		final List<Worklog> worklogItems = new ArrayList<Worklog>();
		for (String[] line:splitLines) {
			try {
			numberOfLines ++;
			final Worklog worklog = parseWorklogItemByToken(line, projectMapping, activityMapping);
			if (worklog != null) {
				worklogItems.add(worklog);
			}			
			} catch (Exception e) {
				logger.warn("Error importing line. " + e.getMessage() + " Line: " + line);
				parseErrors++;
			}
		}
		return worklogItems;
	}	

	public Worklog parseWorklogItemByToken(String[] tokens, Map<String, ProjectInvoiceHolder> projectMapping, Map<String, Activity> activityMapping) {
		try {
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
	
	public Set<ExternalActivity> parseExternalActivitys() {
		final Set<ExternalActivity> activitySet = new LinkedHashSet<ExternalActivity>();
		if (canExtractExternalActivity()) {
			for (String[] tokens:splitLines) {
				final ExternalActivity activity = parseExternalActivitySplit(tokens);
				if (activity != null) {
					activitySet.add(activity);
				}				
			}
		}
		return activitySet;
	}

	public Set<ExternalProject> parseExternalProjects() {
		final Set<ExternalProject> projectSet = new LinkedHashSet<ExternalProject>();
		if (canExtractExternalProject()) {
			for (String[] tokens:splitLines) {
				final ExternalProject project = parseExternalProjectSplit(tokens);
				if (project != null) {
						projectSet.add(project);
					}
				}
		}
		return projectSet;
	}	
	
	private List<String[]> splitAllLines(final Reader reader) throws IOException {
		final List<String[]> result = new LinkedList<String[]>();
		CSVReader csvReader = new CSVReader(reader);
			int lineNumber = 0;
		    String [] nextLine;
		    while ((nextLine = csvReader.readNext()) != null) {
				if (lineNumber == 1 && skipFirstLine()) {
					continue;
				}
				result.add(nextLine);
		    }
		csvReader.close();
		return result;
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

	public ExternalActivity parseExternalActivitySplit(String[] tokens) {
		return new ExternalActivity(tokens[3], tokens[3]);
	}
	
	/**
	 * @see cz.softinel.retra.worklog.web.ImportDataParser#parseExternalProject(java.lang.String)
	 */
	public ExternalProject parseExternalProjectSplit(String[] tokens) {
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

	@Override
	public Worklog parseWorklogItem(String line, Map<String, ProjectInvoiceHolder> projectMapping, Map<String, Activity> activityMapping) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalProject parseExternalProject(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExternalActivity parseExternalActivity(String line) {
		// TODO Auto-generated method stub
		return null;
	}

}
