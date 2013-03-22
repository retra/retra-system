package cz.softinel.retra.worklog.web;


/**
 * Command object for the Worklog Invoice pair
 * 
 * @author petr
 */
public class WorklogInvoicePairForm {
	
	private Long[] confirmedItems;

	private String invoice;
	private String project;
	private String activity;
	private String dateFrom;
	private String dateTo;

//	/**
//	 * Prepares project and activity mappings.
//	 * @param parser parser to use to read import data
//	 * @return number of parse errors
//	 * @throws IOException fatal error reading import data
//	 */
//	public int prepareMapping(ImportDataParser parser) throws IOException {
//		
//		final int numberOfLines;
//		final int parseErrors;
//
//		// read the import data and create sets of projects and activities
//		final Set<ExternalActivity> activitySet;
//		final Set<ExternalProject> projectSet;
//		if (parser.canExtractExternalActivity() || parser.canExtractExternalProject()) {
//			parser.init(importData);
//			activitySet = parser.parseExternalActivitys();
//			projectSet = parser.parseExternalProjects();
//			numberOfLines = parser.getNumberOfLines();
//			parseErrors = parser.getParseErrors();
//		} else {
//			activitySet = Collections.emptySet();
//			projectSet = Collections.emptySet();
//			numberOfLines = 0;
//			parseErrors = 0;
//		}
//		
//		allLinesBad = (numberOfLines != 0) && (parseErrors == numberOfLines);
//		
//		if(parser.canExtractExternalProject()) {
//			externalProjects = new ArrayList<ExternalProject>(projectSet);
//			selectedProject = null;
//		} else {
//			selectedProject = new Project();
//			externalProjects = null;
//		}
//		
//		if(parser.canExtractExternalActivity()) {
//			externalActivities = new ArrayList<ExternalActivity>(activitySet);
//			selectedActivity = null;
//		} else {
//			selectedActivity = new Activity();
//			externalActivities = null;
//		}
//		
//		this.freshImportData = false;
//		
//		return parseErrors;
//	}
	
//	/**
//	 * Prepares worklog items according to the import data 
//	 * and selected project and activity mappings.
//	 * @param parser parser to use to read import data
//	 * @param projects list of all Retra projects
//	 * @param activities list of all Retra activities
//	 * @return number of parse errors
//	 * @throws IOException fatal error reading import data
//	 */
//	public int prepareWorklogItems(ImportDataParser parser, List<Project> projects, List<Activity> activities) throws IOException {
//		
////		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(importData), importDataEncoding));
////		String line;
////		int parseErrors = 0;
//	
//		// create a table of valid projects and activities
//		final Map<String, Project> projectMapping = createProjectMapping(projects, parser);
//		final Map<Long, Activity> activityMapping = createActivityMapping(activities, parser);
//		
//		// construct a list of worklog items
//		parser.init(importData);
//		worklogItems = parser.parseWorklogItems(projectMapping, activityMapping);
//		
//		return parser.getParseErrors();
//	}
	
//	/**
//	 * Creates a map from user selected activity mapping 
//	 * and all Mira activities.
//	 * @param activities all available activities
//	 * @return activity mapping
//	 */
//	private Map<Long, Activity> createActivityMapping(List<Activity> activities, ImportDataParser parser) {
//		Map<Long, Activity> activityMapping = new HashMap<Long, Activity>();
//		
//		if(parser.canExtractExternalActivity()) {
//			for (ExternalActivity externalActivity : externalActivities) {
//				Activity activityMatch = null;
//				for (Activity activity : activities) {
//					if (activity.getPk().equals(externalActivity.getActivityId())) {
//						activityMatch = activity;
//						break;
//					}
//				}
//				activityMapping.put(externalActivity.getId(), activityMatch);
//			}
//		} else { 
//			// in this case user had to select appropriate internal activity
//			// that will be applied to all worklog items
//			Activity activityMatch = null;
//			for (Activity activity : activities) {
//				if (activity.getPk().equals(selectedActivity.getPk())) {
//					activityMatch = activity;
//					break;
//				}
//			}
//			activityMapping.put(selectedActivity.getPk(), activityMatch);
//		}
//		
//		return activityMapping;
//	}

//	/**
//	 * Creates a map from user selected project mapping 
//	 * and all Mira projects.
//	 * @param projects all available projects
//	 * @return project mapping
//	 */
//	private Map<String, Project> createProjectMapping(List<Project> projects, ImportDataParser parser) {
//		final Map<String, Project> projectMapping = new HashMap<String, Project>();
//		if (parser.canExtractExternalProject()) {
//			for (ExternalProject externalProject : externalProjects) {
//				Project projectMatch = null;
//				for (Project project : projects) {
//					if (project.getPk().equals(externalProject.getProjectId())) {
//						projectMatch = project;
//						break;
//					}
//				}
//				projectMapping.put(externalProject.getId(), projectMatch);
//			}
//		} else {
//			for (final Project project : projects) {
//				forEachImportRules(new ImportRuleHandler() {
//					public void handle(final String[] parts) {
//						if (parts.length == 3) {
//							final String ruleName = parts[0];
//							final String externalProjectId = parts[1];
//							final String projectId = parts[2];
//							if ("project".equals(ruleName) && (projectId.equals(""+project.getPk()) || projectId.equals(project.getCode()))) {
//								projectMapping.put(externalProjectId, project);
//							}
//						}
//					}
//				});
//			}
//			// Add default value: in this case user had to select appropriate internal project
//			// that will be used for creation of all worklog items
//			for (final Project project : projects) {
//				if (project.getPk().equals(selectedProject.getPk())) {
//					projectMapping.put("", project);
//					break;
//				}
//			}
//		}
//		return projectMapping;
//	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public Long[] getConfirmedItems() {
		return confirmedItems;
	}

	public void setConfirmedItems(Long[] confirmedItems) {
		this.confirmedItems = confirmedItems;
	}
	
}
