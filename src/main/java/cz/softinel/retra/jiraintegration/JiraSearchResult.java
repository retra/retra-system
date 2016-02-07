package cz.softinel.retra.jiraintegration;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraSearchResult implements Serializable {
	
	private String expand;
	private Integer startAt;
	private Integer maxResults;
	private Integer total;
	
	private List<JiraIssue> issues;

	public String getExpand() {
		return expand;
	}

	public void setExpand(String expand) {
		this.expand = expand;
	}

	public Integer getStartAt() {
		return startAt;
	}

	public void setStartAt(Integer startAt) {
		this.startAt = startAt;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<JiraIssue> getIssues() {
		return issues;
	}

	public void setIssues(List<JiraIssue> issues) {
		this.issues = issues;
	}
	
}
