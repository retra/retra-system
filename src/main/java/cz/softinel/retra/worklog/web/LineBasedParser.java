package cz.softinel.retra.worklog.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.Worklog;

/**
 * Base class for parsers which parse text data line by line.
 * 
 * @author radek
 */
public abstract class LineBasedParser implements ImportDataParser {

	private final String encoding;

	protected static Log logger = LogFactory.getLog(LineBasedParser.class);

	public LineBasedParser(final String encoding) {
		this.encoding = encoding;
	}

	private List<String> lines = null;
	protected int numberOfLines;
	protected int parseErrors;

	public void init(byte[] data) throws IOException {
		final Reader reader = getDataReader(data);
		lines = splitLines(reader);
	}

	public Set<ExternalActivity> parseExternalActivitys() {
		final Set<ExternalActivity> activitySet = new LinkedHashSet<ExternalActivity>();
		if (canExtractExternalActivity()) {
			doForEachLine(new LineProcessor() {
				public void process(final String line) {
					final ExternalActivity activity = parseExternalActivity(line);
					if (activity != null) {
						activitySet.add(activity);
					}
				}
			});
		}
		return activitySet;
	}

	public Set<ExternalProject> parseExternalProjects() {
		final Set<ExternalProject> projectSet = new LinkedHashSet<ExternalProject>();
		if (canExtractExternalProject()) {
			doForEachLine(new LineProcessor() {
				public void process(final String line) {
					final ExternalProject project = parseExternalProject(line);
					if (project != null) {
						projectSet.add(project);
					}
				}
			});
		}
		return projectSet;
	}

	public List<Worklog> parseWorklogItems(final Map<String, ProjectInvoiceHolder> projectMapping, final Map<String, Activity> activityMapping) {
		final List<Worklog> worklogItems = new ArrayList<Worklog>();
		doForEachLine(new LineProcessor() {
			public void process(final String line) {
				final Worklog worklog = parseWorklogItem(line, projectMapping, activityMapping);
				if (worklog != null) {
					worklogItems.add(worklog);
				}
			}
		});
		return worklogItems;
	}
	
	protected Reader getDataReader(final byte[] data) {
		try {
			return new InputStreamReader(new ByteArrayInputStream(data), encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected List<String> splitLines(final Reader reader) throws IOException {
		final List<String> result = new LinkedList<String>();
		final BufferedReader bufferedReader = new BufferedReader(reader);
		int lineNumber = 0;
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lineNumber++;
			if (lineNumber == 1 && skipFirstLine()) {
				continue;
			}
			result.add(line);
		}
		return result;
	}

	protected static interface LineProcessor {
		public void process(final String line);
	}

	protected void doForEachLine(final LineProcessor lineProcessor) {
		if (lines == null) {
			throw new IllegalStateException("Parser was not initialized.");
		}
		parseErrors = 0;
		numberOfLines = 0;
		for (final String line : lines) {
			try {
				numberOfLines ++;
				lineProcessor.process(line);
			} catch (Exception e) {
				logger.warn("Error importing line. " + e.getMessage() + " Line: " + line);
				parseErrors++;
			}
		}
	}

	public int getParseErrors() {
		return parseErrors;
	}
	
	public int getNumberOfLines() {
		return numberOfLines;
	}
	
	
	/**
	 * Parses one line and extracts information about project.
	 * 
	 * @param line
	 *            worklog item line
	 * @return item's project
	 */
	public abstract ExternalProject parseExternalProject(String line);

	/**
	 * Parses one line and extracts information about activity.
	 * 
	 * @param line
	 *            worklog item line
	 * @return item's activity
	 */
	public abstract ExternalActivity parseExternalActivity(String line);

	/**
	 * Parses one line and creates whole worklog item.
	 * 
	 * @param line
	 *            worklog item line
	 * @param projectMapping
	 *            mapping between imported projects and Mira projects
	 * @param activityMapping
	 *            mapping between imported activities and Mira activities
	 * @return worklog item, <code>null</code> if item cannot be mapped
	 */
	public abstract Worklog parseWorklogItem(String line,
			Map<String, ProjectInvoiceHolder> projectMapping,
			Map<String, Activity> activityMapping);

	/**
	 * Whether to skip first line of file or not. This is usually the case if
	 * first line contains header (the name of fields).
	 * 
	 * @return
	 */
	public abstract boolean skipFirstLine();

}
