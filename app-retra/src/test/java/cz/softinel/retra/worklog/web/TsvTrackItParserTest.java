package cz.softinel.retra.worklog.web;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;

import org.junit.Before;
import org.junit.Test;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.project.Project;
import cz.softinel.retra.worklog.Worklog;


/**
 *
 * @author Pavel Mueller
 */
public class TsvTrackItParserTest {
	
	private TsvTrackItParser parser;
	
	private static final String TEST_LINE = 
		"5.2.2007\t1017\tAll-Allianz0951-Direct Sales\t106\tProgramming\t08:15\t10:00\t01:45\tPolske travel";
	private static final String ERROR_LINE = 
		"5.2.2007\t1017\tAll-Allianz0951-Direct";
	
	public static void main(String[] args) {
		TestRunner.run(new JUnit4TestAdapter(TsvTrackItParserTest.class));
	}
	
	@Before
	public void setUp() {
		parser = new TsvTrackItParser("UTF-8");
	}
	
	@Test
	public void testParseProject() {
		ExternalProject project = parser.parseExternalProject(TEST_LINE);
		assertEquals("1017", project.getId());
		assertEquals("All-Allianz0951-Direct Sales", project.getName());
	}
	
	@Test(expected=ImportParsingException.class)
	public void testParseProjectError() {
		parser.parseExternalProject(ERROR_LINE);
	}
	
	@Test
	public void testParseActivity() {
		ExternalActivity activity = parser.parseExternalActivity(TEST_LINE);
		assertEquals(new Long(106), activity.getId());
		assertEquals("Programming", activity.getName());
	}
	
	@Test(expected=ImportParsingException.class)
	public void testParseActivityError() {
		parser.parseExternalActivity(ERROR_LINE);
	}

	@Test
	public void testParseWorklogItem() {
		Map<String, ProjectInvoiceHolder> projectMapping = new HashMap<String, ProjectInvoiceHolder>();
		ProjectInvoiceHolder project = new ProjectInvoiceHolder();
		projectMapping.put("1017", project);
		
		Map<String, Activity> activityMapping = new HashMap<String, Activity>();
		Activity activity = new Activity();
		activityMapping.put("106", activity);
		
		Worklog worklog = parser.parseWorklogItem(TEST_LINE, projectMapping, activityMapping);
		assertEquals(activity, worklog.getActivity());
		assertEquals(project, worklog.getProject());
		assertEquals("Polske travel", worklog.getDescription());
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(2007, 1, 5, 8, 15);
		assertEquals(cal.getTimeInMillis(), worklog.getWorkFrom().getTime());
		cal.setTimeInMillis(0);
		cal.set(2007, 1, 5, 10, 00);
		assertEquals(cal.getTimeInMillis(), worklog.getWorkTo().getTime());
	}

	@Test
	public void testParseWorklogItemNoMapping() {
		Map<String, ProjectInvoiceHolder> projectMapping = new HashMap<String, ProjectInvoiceHolder>();
		ProjectInvoiceHolder project = new ProjectInvoiceHolder();
		projectMapping.put("666", project); //mapping only for other project
		
		Map<String, Activity> activityMapping = new HashMap<String, Activity>();
		Activity activity = new Activity();
		activityMapping.put("106", activity);
		
		Worklog worklog = parser.parseWorklogItem(TEST_LINE, projectMapping, activityMapping);
		assertNull(worklog);
	}
	
	
	@Test(expected=ImportParsingException.class)
	public void testParseWorklogItemError() {
		parser.parseExternalActivity(ERROR_LINE);
	}

}
