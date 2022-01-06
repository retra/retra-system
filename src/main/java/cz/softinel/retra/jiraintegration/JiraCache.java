package cz.softinel.retra.jiraintegration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class JiraCache {

	private CacheManager manager;
	private Cache jiraCache;

	public JiraCache() {
		this.manager = CacheManager.create();
		this.jiraCache = new Cache("JIRA-ISSUES-CACHE", 25000, true, false, 0, 0);
		this.manager.addCache(jiraCache);
	}

	public JiraIssue getIssueFromCache(final String key) {
		Element element = jiraCache.get(key);
		if (element != null && element.getObjectValue() != null) {
			return (JiraIssue) element.getObjectValue();
		}
		return null;
	}

	public void addIssueToCache(final JiraIssue issue) {
		if (issue != null) {
			Element element = new Element(issue.getKey(), issue);
			jiraCache.put(element);
		}
	}

	public void clearCache() {
		jiraCache.removeAll(true);
	}
}
