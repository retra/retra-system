package cz.softinel.retra.jiraintegration;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class JiraHelperTest extends Assert {

	@Test
	public void findIssueCodesInText() {
		String text = "Bla fdsjfnhsdkj jfnsjdk INT-1 fjkdsjfhds INT-2";
		List<String> codes = JiraHelper.findIssueCodesInText(text);
		Assert.assertEquals(2, codes.size());
	}

	@Test
	public void findIssueCodesInTextTabStart() {
		String text = "\tINT-123\n\r";
		List<String> codes = JiraHelper.findIssueCodesInText(text);
		Assert.assertEquals(1, codes.size());
	}

}
