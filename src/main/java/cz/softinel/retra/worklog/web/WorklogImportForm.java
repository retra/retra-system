package cz.softinel.retra.worklog.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.Worklog;

/**
 * Command object for the Worklog Import wizard
 *
 * @version $Revision: 1.6 $ $Date: 2007-11-26 18:50:54 $
 * @author Pavel Mueller
 * @see WorklogImportController
 */
public class WorklogImportForm {

	private static Logger logger = LoggerFactory.getLogger(WorklogImportForm.class);

	private static final String SEPARATING_DELIMITER = "a";
	private static final String MAPPING_DELIMITER = "x";

	public static enum ImportType {
		TYPE_TRACKIT, TYPE_OUTLOOK_EXPRESS, TYPE_CSV
	};

	private byte[] importData;
	private String importDataEncoding;
	private ImportType importType;

	private List<Worklog> worklogItems = new ArrayList<Worklog>();
	private int[] confirmedItems;

	private List<ExternalProject> externalProjects = new ArrayList<ExternalProject>();
	private List<ExternalActivity> externalActivities = new ArrayList<ExternalActivity>();

	/*
	 * These values are used only if parser is not able to parse external project or
	 * (respectively) external activity from import file. In that case user has to
	 * select appropriate values.
	 */
	private Project selectedProject = new Project();
	private Activity selectedActivity = new Activity();

	private String importRules;
	private boolean showImportRules = true;

	public boolean isShowImportRules() {
		return showImportRules;
	}

	public void setShowImportRules(boolean showImportRules) {
		this.showImportRules = showImportRules;
	}

	private Long selectedInvoice;

	public Long getSelectedInvoice() {
		return selectedInvoice;
	}

	public void setSelectedInvoice(Long selectedInvoice) {
		this.selectedInvoice = selectedInvoice;
	}

	private boolean freshImportData;

	/**
	 * if all lines are bad in uploaded file
	 */
	private boolean allLinesBad;

	/**
	 * Prepares project and activity mappings.
	 * 
	 * @param parser parser to use to read import data
	 * @return number of parse errors
	 * @throws IOException fatal error reading import data
	 */
	public int prepareMapping(ImportDataParser parser, List<Project> projects, List<Activity> activities)
			throws IOException {
		final int numberOfLines;
		final int parseErrors;
		showImportRules = parser.canSpecifyImportRules();
		// read the import data and create sets of projects and activities
		final Set<ExternalActivity> activitySet;
		final Set<ExternalProject> projectSet;
		if (parser.canExtractExternalActivity() || parser.canExtractExternalProject()) {
			parser.init(importData);
			activitySet = parser.parseExternalActivitys();
			for (ExternalActivity ea : activitySet) {
				for (Activity a : activities) {
					if (a.getCode().equalsIgnoreCase(ea.getId())) {
						ea.setActivityId(a.getPk());
						break;
					}
				}
			}
			projectSet = parser.parseExternalProjects();
			for (ExternalProject ep : projectSet) {
				for (Project p : projects) {
					if (p.getCode().equalsIgnoreCase(ep.getId())) {
						ep.setProjectId(p.getPk());
						break;
					}
					if (p.getCode().indexOf(ep.getId()) != -1) {
						ep.setProjectId(p.getPk());
					}
				}
			}
			numberOfLines = parser.getNumberOfLines();
			parseErrors = parser.getParseErrors();
		} else {
			activitySet = Collections.emptySet();
			projectSet = Collections.emptySet();
			numberOfLines = 0;
			parseErrors = 0;
		}

		allLinesBad = (numberOfLines != 0) && (parseErrors == numberOfLines);

		if (parser.canExtractExternalProject()) {
			externalProjects = new ArrayList<ExternalProject>(projectSet);
			selectedProject = null;
		} else {
			selectedProject = new Project();
			externalProjects = null;
		}

		if (parser.canExtractExternalActivity()) {
			externalActivities = new ArrayList<ExternalActivity>(activitySet);
			selectedActivity = null;
		} else {
			selectedActivity = new Activity();
			externalActivities = null;
		}

		this.freshImportData = false;

		return parseErrors;
	}

	/**
	 * Prepares worklog items according to the import data and selected project and
	 * activity mappings.
	 * 
	 * @param parser     parser to use to read import data
	 * @param projects   list of all Retra projects
	 * @param activities list of all Retra activities
	 * @return number of parse errors
	 * @throws IOException fatal error reading import data
	 */
	public int prepareWorklogItems(ImportDataParser parser, List<Project> projects, List<Activity> activities,
			List<Invoice> invoices) throws IOException {

//		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(importData), importDataEncoding));
//		String line;
//		int parseErrors = 0;

		// create a table of valid projects and activities
		final Map<String, ProjectInvoiceHolder> projectMapping = createProjectMapping(projects, invoices, parser);
		final Map<String, Activity> activityMapping = createActivityMapping(activities, parser);

		// construct a list of worklog items
		parser.init(importData);
		worklogItems = parser.parseWorklogItems(projectMapping, activityMapping);

		return parser.getParseErrors();
	}

	/**
	 * Creates a map from user selected activity mapping and all Mira activities.
	 * 
	 * @param activities all available activities
	 * @return activity mapping
	 */
	private Map<String, Activity> createActivityMapping(List<Activity> activities, ImportDataParser parser) {
		Map<String, Activity> activityMapping = new HashMap<String, Activity>();

		if (parser.canExtractExternalActivity()) {
			for (ExternalActivity externalActivity : externalActivities) {
				Activity activityMatch = null;
				for (Activity activity : activities) {
					if (activity.getPk().equals(externalActivity.getActivityId())) {
						activityMatch = activity;
						break;
					}
				}
				activityMapping.put(externalActivity.getId(), activityMatch);
			}
		} else {
			// in this case user had to select appropriate internal activity
			// that will be applied to all worklog items
			Activity activityMatch = null;
			for (Activity activity : activities) {
				if (activity.getPk().equals(selectedActivity.getPk())) {
					activityMatch = activity;
					break;
				}
			}
			activityMapping.put(selectedActivity.getPk().toString(), activityMatch);
		}

		return activityMapping;
	}

	/**
	 * Creates a map from user selected project mapping and all Mira projects.
	 * 
	 * @param projects all available projects
	 * @return project mapping
	 */
	private Map<String, ProjectInvoiceHolder> createProjectMapping(List<Project> projects, List<Invoice> invoices,
			ImportDataParser parser) {
		final Map<String, ProjectInvoiceHolder> projectMapping = new HashMap<String, ProjectInvoiceHolder>();
		if (parser.canExtractExternalProject()) {
			for (ExternalProject externalProject : externalProjects) {
				Project projectMatch = null;
				for (Project project : projects) {
					if (project.getPk().equals(externalProject.getProjectId())) {
						projectMatch = project;
						break;
					}
				}
				Invoice invoiceMatch = null;
				for (Invoice invoice : invoices) {
					if (invoice.getPk().equals(externalProject.getInvoiceId())) {
						invoiceMatch = invoice;
						break;
					}
				}
				ProjectInvoiceHolder piHolder = new ProjectInvoiceHolder();
				piHolder.setInvoice(invoiceMatch);
				piHolder.setProject(projectMatch);
				projectMapping.put(externalProject.getId(), piHolder);
			}
		} else {
			for (final Project project : projects) {
				forEachImportRules(new ImportRuleHandler() {
					public void handle(final String[] parts) {
						if (parts.length == 3) {
							final String ruleName = parts[0];
							final String externalProjectId = parts[1];
							final String projectId = parts[2];
							if ("project".equals(ruleName) && (projectId.equals("" + project.getPk())
									|| projectId.equals(project.getCode()))) {
								ProjectInvoiceHolder piHolder = new ProjectInvoiceHolder();
								piHolder.setProject(project);
								projectMapping.put(externalProjectId, piHolder);
							}
						}
					}
				});
			}
			// Add default value: in this case user had to select appropriate internal
			// project
			// that will be used for creation of all worklog items
			for (final Project project : projects) {
				if (project.getPk().equals(selectedProject.getPk())) {
					ProjectInvoiceHolder piHolder = new ProjectInvoiceHolder();
					piHolder.setProject(project);
					projectMapping.put("", piHolder);
					break;
				}
			}
		}
		return projectMapping;
	}

	private static interface ImportRuleHandler {
		void handle(String[] parts);
	}

	private void forEachImportRules(final ImportRuleHandler handler) {
		try {
			if (importRules != null) {
				final BufferedReader br = new BufferedReader(new StringReader(importRules));
				String line;
				while ((line = br.readLine()) != null) {
					final String[] parts = line.split(":");
					handler.handle(parts);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Returns a list of user confirmed worklog items to import.
	 * 
	 * @return list of confirmed worklogs
	 */
	public List<Worklog> getConfirmedWorklogItems() {
		List<Worklog> items = new ArrayList<Worklog>();
		if (confirmedItems != null) {
			for (int i = 0; i < confirmedItems.length; i++) {
				items.add(worklogItems.get(confirmedItems[i] - 1));
			}
		}
		return items;
	}

	/**
	 * Returns all external activities
	 * 
	 * @return the externalActivities
	 */
	public List<ExternalActivity> getExternalActivities() {
		return externalActivities;
	}

	/**
	 * Returns all external projects
	 * 
	 * @return the externalProjects
	 */
	public List<ExternalProject> getExternalProjects() {
		return externalProjects;
	}

	public Project getSelectedProject() {
		return this.selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}

	public Activity getSelectedActivity() {
		return this.selectedActivity;
	}

	public void setSelectedActivity(Activity selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	public String getImportRules() {
		return importRules;
	}

	public void setImportRules(String importRules) {
		this.importRules = importRules;
	}

	/**
	 * @return the importData
	 */
	public byte[] getImportData() {
		return importData;
	}

	/**
	 * Sets imported data.
	 * 
	 * @param importData the importData to set
	 */
	public void setImportData(byte[] importData) {
		this.importData = importData;
		this.freshImportData = true;
	}

	public String getImportDataEncoding() {
		return importDataEncoding;
	}

	public void setImportDataEncoding(String importDataEncoding) {
		this.importDataEncoding = importDataEncoding;
	}

	/**
	 * Flag indicating if the import data has not been parsed yet.
	 * 
	 * @return the freshImportData
	 */
	public boolean isFreshImportData() {
		return freshImportData;
	}

	/**
	 * Returns a selected import type.
	 * 
	 * @return the importType
	 */
	public ImportType getImportType() {
		return importType;
	}

	/**
	 * Sets selected import type.
	 * 
	 * @param importType the importType to set
	 */
	public void setImportType(ImportType importType) {
		this.importType = importType;
	}

	/**
	 * Returns a list of all worklog items.
	 * 
	 * @return the worklogItems
	 */
	public List<Worklog> getWorklogItems() {
		return worklogItems;
	}

	/**
	 * Sets a list of all worklog items.
	 * 
	 * @param worklogItems the worklogItems to set
	 */
	public void setWorklogItems(List<Worklog> worklogItems) {
		this.worklogItems = worklogItems;
	}

	/**
	 * Sets a list of indexes of confirmed worklog items.
	 * 
	 * @param confirmedItems the confirmedItems to set
	 */
	public void setConfirmedItems(int[] confirmedItems) {
		this.confirmedItems = confirmedItems;
	}

	/**
	 * Returns a list of indexes of confirmed worklog items.
	 * 
	 * @return the confirmedItems
	 */
	public int[] getConfirmedItems() {
		return confirmedItems;
	}

	/**
	 * Creates a cookie value for project mapping.
	 * 
	 * @return cookie value
	 */
	public String getProjectMappingCookie() {
		StringBuffer buf = new StringBuffer();
		for (ExternalProject project : externalProjects) {
			if (!project.getProjectId().equals(Long.valueOf(-1L))) {
				buf.append(project.getId()).append(MAPPING_DELIMITER);
				buf.append(project.getProjectId()).append(SEPARATING_DELIMITER);
			}
		}
		return buf.toString();
	}

	/**
	 * Creates a cookie value for activity mapping.
	 * 
	 * @return cookie value
	 */
	public String getActivityMappingCookie() {
		StringBuffer buf = new StringBuffer();
		for (ExternalActivity activity : externalActivities) {
			if (!activity.getActivityId().equals(Long.valueOf(-1L))) {
				buf.append(activity.getId()).append(MAPPING_DELIMITER);
				buf.append(activity.getActivityId()).append(SEPARATING_DELIMITER);
			}
		}
		return buf.toString();
	}

	/**
	 * Configures project mapping according to the cookie value.
	 * 
	 * @param value cookie value
	 */
	public void setProjectMappingFromCookie(String value) {
		try {
			String[] mappings = StringUtils.delimitedListToStringArray(value, SEPARATING_DELIMITER);
			for (int i = 0; i < mappings.length - 1; i++) {
				String[] mapping = StringUtils.delimitedListToStringArray(mappings[i], MAPPING_DELIMITER);
				String id = mapping[0];
				Long projectId = Long.parseLong(mapping[1]);
				for (ExternalProject project : externalProjects) {
					if (project.getId().equals(id)) {
						project.setProjectId(projectId);
					}
				}
			}
		} catch (Exception e) {
			// ignore cookie import
			logger.warn("Error parsing project mapping cookie.", e);
		}
	}

	/**
	 * Configures activity mapping according to the cookie value.
	 * 
	 * @param value cookie value
	 */
	public void setActivityMappingFromCookie(String value) {
		try {
			String[] mappings = StringUtils.delimitedListToStringArray(value, SEPARATING_DELIMITER);
			for (int i = 0; i < mappings.length - 1; i++) {
				String[] mapping = StringUtils.delimitedListToStringArray(mappings[i], MAPPING_DELIMITER);
				Long id = Long.parseLong(mapping[0]);
				Long activityId = Long.parseLong(mapping[1]);
				for (ExternalActivity activity : externalActivities) {
					if (activity.getId().equals(id)) {
						activity.setActivityId(activityId);
					}
				}
			}
		} catch (Exception e) {
			// ignore cookie import
			logger.warn("Error parsing activity mapping cookie.", e);
		}
	}

	public boolean isAllLinesBad() {
		return allLinesBad;
	}

	
}
