package cz.softinel.retra.worklog.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.util.StringUtils;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.Worklog;

/**
 * Parser for tab-separated data format from Track-IT light.
 *
 * @version $Revision: 1.4 $ $Date: 2007-11-26 18:50:54 $
 * @author Pavel Mueller
 */
public class TsvTrackItParser extends LineBasedParser {
	
	private static final int MIN_LINE_TOKENS = 8;
	private static final String FIELD_DELIMITER = "\t";
	private static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm";
	private static final String ENGLISH_DATETIME_FORMAT = "MM/dd/yyyy HH:mm";
	
	private static final String PROJECT_MAPPING_COOKIE = "mira.worklog.tsv.mapping.project";
	private static final String ACTIVITY_MAPPING_COOKIE = "mira.worklog.tsv.mapping.activity";
	
	private SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
	private SimpleDateFormat englishFormatter = new SimpleDateFormat(ENGLISH_DATETIME_FORMAT);
	
	public TsvTrackItParser(final String encoding) {
		super(encoding);
	}

	/**
	 * @see cz.softinel.retra.worklog.web.ImportDataParser#parseExternalActivity(java.lang.String)
	 */
	public ExternalActivity parseExternalActivity(String line) {
		String tokens[] = StringUtils.delimitedListToStringArray(line, FIELD_DELIMITER);
		if (tokens.length < MIN_LINE_TOKENS) {
			throw new ImportParsingException("Line has less than " + MIN_LINE_TOKENS + " fields.");
		}
		
		return new ExternalActivity(tokens[3], tokens[4]);
	}
	
	/**
	 * @see cz.softinel.retra.worklog.web.ImportDataParser#parseExternalProject(java.lang.String)
	 */
	public ExternalProject parseExternalProject(String line) {
		String tokens[] = StringUtils.delimitedListToStringArray(line, FIELD_DELIMITER);
		if (tokens.length < MIN_LINE_TOKENS) {
			throw new ImportParsingException("Line has less than " + MIN_LINE_TOKENS + " fields.");
		}
		return new ExternalProject(tokens[1], tokens[2]);
	}
	
	/**
	 * @see cz.softinel.retra.worklog.web.ImportDataParser#parseWorklogItem(java.lang.String, java.util.Map, java.util.Map)
	 */
	public Worklog parseWorklogItem(String line, Map<String, ProjectInvoiceHolder> projectMapping, Map<String, Activity> activityMapping) {
		String tokens[] = StringUtils.delimitedListToStringArray(line, FIELD_DELIMITER);
		if (tokens.length < MIN_LINE_TOKENS) {
			throw new ImportParsingException("Line has less than "+MIN_LINE_TOKENS+" fields.");
		}

		Worklog worklog = new Worklog();
		
		try {
			ProjectInvoiceHolder holder = projectMapping.get(tokens[1]);
			Project project=null;
			if (holder!=null) {
				project=holder.getProject();
			}
			Activity activity = activityMapping.get(tokens[3]);
			if (project == null || activity == null) {
				// item should not be imported
				return null;
			}

			// set worklog project and activity
			worklog.setProject(project);
			worklog.setActivity(activity);
			
			// set worklog range
			String dateFrom = tokens[0] + " " + tokens[5];
			String dateTo = tokens[0] + " " + tokens[6];
			try {
				worklog.setWorkFrom(formatter.parse(dateFrom));
				worklog.setWorkTo(formatter.parse(dateTo));
			} catch (ParseException e) {
				// try english formatter if the czech one is not working
				worklog.setWorkFrom(englishFormatter.parse(dateFrom));
				worklog.setWorkTo(englishFormatter.parse(dateTo));
			}
			
			// set description in any
			if (tokens.length > MIN_LINE_TOKENS) {
				worklog.setDescription(tokens[8]);
			}
		} catch (Exception e) {
			throw new ImportParsingException(e.getMessage());
		}
		
		return worklog;
	}

	public boolean canExtractExternalActivity() {
		return true;
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
		return true;
	}

}
