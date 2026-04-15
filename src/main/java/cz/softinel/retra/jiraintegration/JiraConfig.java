package cz.softinel.retra.jiraintegration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import cz.hb.lib.jira.JiraService;
import cz.hb.lib.jira.instance.JiraInstance;
import cz.softinel.helper.Jahe;
import cz.softinel.textconfig.TextConfig;
import cz.softinel.textconfig.TextConfigHelper;

public class JiraConfig implements InitializingBean {

	private boolean enabled;
//	private String user;
//	private String password;
//	private String baseUrl;
//	private String issuePath;
//	private String restPath;
	
	private String hbUrl;
	private String kbcUrl;
	private Resource jiraProperties;
	
	public static final JiraInstance[] ACTIVE = Jahe.array(
			JiraInstance.HB, JiraInstance.KBC
	);
	
	private JiraService jiraService;
	private JiraCache jiraCache;
	
	/** {@inheritDoc}
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet() */
	@Override
	public void afterPropertiesSet() throws Exception {
		final TextConfig config = TextConfigHelper.load(jiraProperties.getFile());
		jiraService.setupConfig(config, ACTIVE);
		jiraService.getInstances(true);
	}
	
	public String getJiraUrl(final String jiraKey) {
		// FIXME pincr: Tohle je jen provizorni reseni, ale ono se to stejne casem vyhodi
		if (Jahe.notEmpty(jiraKey)) {
			if (jiraKey.startsWith("HB") || jiraKey.startsWith("VAL-")) {
				return kbcUrl;
			}
		}
		return hbUrl;
	}
	public String getIssueUrl(final String jiraKey) {
		return getJiraUrl(jiraKey) + "/" + jiraKey;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getHbUrl() {
		return hbUrl;
	}
	public void setHbUrl(String hbUrl) {
		this.hbUrl = hbUrl;
	}
	
	public String getKbcUrl() {
		return kbcUrl;
	}
	public void setKbcUrl(String kbcUrl) {
		this.kbcUrl = kbcUrl;
	}
	
	public Resource getJiraProperties() {
		return jiraProperties;
	}
	public void setJiraProperties(Resource jiraProperties) {
		this.jiraProperties = jiraProperties;
	}

	public JiraService getJiraService() {
		return jiraService;
	}
	public void setJiraService(JiraService jiraService) {
		this.jiraService = jiraService;
	}

	public JiraCache getJiraCache() {
		return jiraCache;
	}
	public void setJiraCache(JiraCache jiraCache) {
		this.jiraCache = jiraCache;
	}
	
//	public String getUser() {
//		return user;
//	}
//
//	public void setUser(String user) {
//		this.user = user;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public String getBaseUrl() {
//		return baseUrl;
//	}
//
//	public void setBaseUrl(String baseUrl) {
//		this.baseUrl = baseUrl;
//	}
//
//	public String getIssuePath() {
//		return issuePath;
//	}
//
//	public void setIssuePath(String issuePath) {
//		this.issuePath = issuePath;
//	}
//
//	public String getRestPath() {
//		return restPath;
//	}
//
//	public void setRestPath(String restPath) {
//		this.restPath = restPath;
//	}

}
