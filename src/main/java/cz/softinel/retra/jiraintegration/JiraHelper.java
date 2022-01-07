package cz.softinel.retra.jiraintegration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import cz.softinel.retra.jiraintegration.logic.JiraLogic;

public abstract class JiraHelper {

	public static final String JIRA_ISSUE_CODE_REGEXP = "[A-Z][A-Z0-9]{0,9}\\-[1-9]{1}[0-9]{0,9}";
	public static final String SW_CODE_PREFIX = "SW-";

	private static final Pattern JIRA_ISSUE_CODE_PATTERN = Pattern.compile(JIRA_ISSUE_CODE_REGEXP);

	private JiraHelper() {
	}

	public static List<String> findIssueCodesInText(final String text) {
		List<String> result = new ArrayList<String>();

		if (text != null) {
			Matcher m = JIRA_ISSUE_CODE_PATTERN.matcher(text);
			while (m.find()) {
				String code = m.group(0);
				if (!code.startsWith(SW_CODE_PREFIX)) {
					result.add(m.group(0));
				}
			}
		}

		return result;
	}

	public static String getLinkableText(final String text, final JiraLogic jiraLogic) {
		return getLinkableText(text, null, jiraLogic);
	}

	public static String getLinkableText(final String text, String linkText, final JiraLogic jiraLogic) {
		String result = null;

		if (jiraLogic != null && jiraLogic.getJiraConfig() != null) {
			StringBuilder urlsb = new StringBuilder("<a href=\"");
			urlsb.append(jiraLogic.getJiraConfig().getBaseUrl());
			urlsb.append(jiraLogic.getJiraConfig().getIssuePath());
			urlsb.append("%s\" title=\"%s\" target=\"_blank\">");
			if (linkText != null) {
				urlsb.append(linkText);
			} else {
				urlsb.append("%s");
			}
			urlsb.append("</a>");

			String url = urlsb.toString();
			List<String> issues = findIssueCodesInText(text);
			if (issues != null && !issues.isEmpty()) {
				result = text;
				for (String code : issues) {
					JiraIssue issue = jiraLogic.getJiraIssue(code);
					String title = code;
					if (issue != null) {
						String summary = getSafeJiraText(issue.getSummary());
						title = code + " - " + summary;
						String replacement = String.format(url, code, title, code);
						result = result.replaceAll(code, replacement);
					}
				}
			} else {
				result = text;
			}
		} else {
			result = text;
		}

		return result;
	}

	public static String getSafeJiraText(final String text) {
		String safeText = "";
		if (text != null) {
			safeText = StringEscapeUtils.escapeHtml(text);
			safeText = safeText.replace("$", "&dollar;").replace("{", "&lcub;").replace("}", "&rcub;");
		}
		return safeText;
	}
}
