package cz.softinel.retra.worklog.web;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.worklog.Worklog;

/**
 * 
 * Parser for tab separated data exported from Outlook express
 * 
 * @author Borisa Zivkovic
 */
public class OutlookExpressParser extends LineBasedParser {

	private static Logger logger = Logger.getLogger(OutlookExpressParser.class);

	private static final int MINIMUM_REQUIRED_NUMBER_OF_TOKENS = 5; // was 5,
																	// 15, 22
	private static final String FIELD_DELIMITER = "\t";

	private static final String CZECH_DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
	private static final String ENGLISH_DATETIME_FORMAT = "MM/dd/yyyy HH:mm";

	private SimpleDateFormat czechFormatter = new SimpleDateFormat(CZECH_DATETIME_FORMAT);
	private SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATETIME_FORMAT);

	public OutlookExpressParser(final String encoding) {
		super(encoding);
	}

	private class LineBuffer {
		private boolean firstLine = true;
		private String firstLineData = null;
		private List<String> lines = new LinkedList<String>();

		private void addLine(final String line) {
			if (line == null || line.trim().length() == 0) {
				return; // Ignore empty lines
			}
			if (firstLine && skipFirstLine()) {
				// Skip this line ...
				firstLineData = line;
			} else {
				lines.add(line);
			}
			firstLine = false;
		}
	}

	@Override
	protected List<String> splitLines(final Reader reader) throws IOException {
		final LineBuffer result = new LineBuffer();
		StringBuilder line = new StringBuilder();
		boolean quotedString = false;
		int character;
		while ((character = reader.read()) != -1) {
			if (character == '"') {
				quotedString = !quotedString;
			} else if (character == '\n') {
				if (quotedString) {
					line.append(" ");
				} else {
					result.addLine(line.toString());
					line = new StringBuilder();
				}
			} else if (character == '\t' && quotedString) {
				line.append(' ');
			} else if (character == ',' && !quotedString) {
				line.append('\t');
			} else {
				line.append((char) character);
			}
		}
		result.addLine(line.toString());

		try {
			setupByFirstLine(result.firstLineData);
		} catch (ImportParsingException e) {
			logger.info("Couldn't setup parser.", e);
			return new ArrayList<String>();
		}

		return result.lines;
	}

	private void setupByFirstLine(final String line) {
		String tokens[] = org.springframework.util.StringUtils.delimitedListToStringArray(line, FIELD_DELIMITER);
		if (tokens.length < MINIMUM_REQUIRED_NUMBER_OF_TOKENS) {
			throw new ImportParsingException("Line has less than " + MINIMUM_REQUIRED_NUMBER_OF_TOKENS + " fields.");
		}
		firstLine = line;
		columnCount = tokens.length;
		descriptionIndex = 0;
		startDateIndex = 1;
		startTimeIndex = 2;
		endDateIndex = 3;
		endTimeIndex = 4;
		if (columnCount < 22) {
			categoryIndex = 5;
			timeFlagIndex = 6;
		} else {
			categoryIndex = 14;
			timeFlagIndex = 21;
		}
	}

	private String firstLine;
	private int columnCount;
	private int descriptionIndex;
	private int startDateIndex;
	private int startTimeIndex;
	private int endDateIndex;
	private int endTimeIndex;
	private int categoryIndex;
	private int timeFlagIndex;

	public Worklog parseWorklogItem(String line, Map<String, ProjectInvoiceHolder> projectMapping, Map<String, Activity> activityMapping) {
		try {
			String tokens[] = org.springframework.util.StringUtils.delimitedListToStringArray(line, FIELD_DELIMITER);
			if (tokens.length != columnCount) {
				throw new ImportParsingException("Line has different count of tokens than " + columnCount + ".");
			}
			Worklog worklog = new Worklog();
			String category = tokens[categoryIndex].trim();
			String timeFlag = tokens[timeFlagIndex].trim();
			if ("4".equals(timeFlag)) {
				return null; // Do not import "out-of-office" items ... TODO
								// pincr: This should be parametrized by
								// importRules!
			}
			ProjectInvoiceHolder project = findProjectInMap(projectMapping, category);
			Activity activity = findActivityInMap(activityMapping);
			worklog.setActivity(activity);
			if (project != null) {
				if (project.getProject()==null) {
					return null;
				}
				worklog.setProject(project.getProject());
				worklog.setInvoice(project.getInvoice());
			} else {
				return null;
			}
			worklog.setDescription(removeQuotes(tokens[descriptionIndex]));
			Date startDateTime = parseDate(tokens[startDateIndex], tokens[startTimeIndex]);
			Date endDateTime = parseDate(tokens[endDateIndex], tokens[endTimeIndex]);
			worklog.setWorkFrom(startDateTime);
			worklog.setWorkTo(endDateTime);
			return worklog;
		} catch (Exception exc) {
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

	private Activity findActivityInMap(Map<String, Activity> activityMap) {
		Activity activity = null;
		Collection<Activity> activities = activityMap.values();
		if (activities == null) {
			throw new IllegalArgumentException("Unable to find internal activity that should be mapped to worklog items");
		}
		if (activities.size() != 1) {
			throw new IllegalArgumentException("Found more than one activity - unable to decide what to map to worklog items");
		}
		activity = activities.iterator().next();
		return activity;
	}

	/**
	 * Removes quotes around worklog item description - if there are any quotes
	 * 
	 * @param str
	 * @return
	 */
	private String removeQuotes(String str) {
		if (str == null) {
			return str;
		}
		String clean = str.trim();
		if (clean.startsWith("\"")) {
			clean = clean.substring(1);
		}
		if (clean.endsWith("\"")) {
			clean = clean.substring(0, clean.length() - 1);
		}
		return clean;
	}

	private Date parseDate(String date, String time) throws ParseException {
		if (StringUtils.isEmpty(date) || StringUtils.isEmpty(time)) {
			throw new IllegalArgumentException("Date or time must not be empty");
		}
		final String fullDateTime = date.trim() + " " + time.trim();
		if (fullDateTime.contains("/")) {
			return englishFormatter.parse(fullDateTime); // Dates with slash use english convention: month/day/year
		} else {
			return czechFormatter.parse(fullDateTime); // Otherwise use czech convention: day.month.year
		}
	}

	/**
	 * @see cz.softinel.retra.worklog.web.ImportDataParser#canExtractExternalActivity()
	 */
	public boolean canExtractExternalActivity() {
		return false;
	}

	public boolean canExtractExternalProject() {
		return true;
	}

	public ExternalActivity parseExternalActivity(String line) {
		return null;
	}

	public ExternalProject parseExternalProject(String line) {
		String tokens[] = org.springframework.util.StringUtils.delimitedListToStringArray(line, FIELD_DELIMITER);
		String category = tokens[categoryIndex].trim();
		String timeFlag = tokens[timeFlagIndex].trim();
		if ("4".equals(timeFlag)) {
			return null; // Do not import "out-of-office" items
		}
		return new ExternalProject(category, category);
	}

	public boolean storesDataToCookies() {
		return false;
	}

	public String getActivityMappingCookieName() {
		return null;
	}

	public String getProjectMappingCookieName() {
		return null;
	}

	public boolean skipFirstLine() {
		return true;
	}

	public boolean canSpecifyImportRules() {
		return true;
	}

}
