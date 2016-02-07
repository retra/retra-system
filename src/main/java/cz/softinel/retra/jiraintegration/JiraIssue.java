package cz.softinel.retra.jiraintegration;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue implements Serializable, Comparable<JiraIssue> {

	private String id;
	private String key;
	private String self;
	
	private String guiLink;
	
	private boolean obalka;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Fields implements Serializable {

		private String summary;
		
		public void setSummary(String summary) {
			this.summary = summary;
		}

	}
	
	private Fields fields;
	
	public int compareTo(final JiraIssue that) {
		return ("" + this.key).compareTo("" + that.key);
	}
	
	@Override
	public boolean equals(final Object that) {
		if (that == null) {
			return false;
		}
		if (that == this) {
			return true;
		}
		if (that instanceof JiraIssue) {
			return ("" + this.key).equals("" + ((JiraIssue)that).key);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ("" + this.key).hashCode();
	}
	
	@Override
	public String toString() {
		return key;
	}
	
	public void setFields(Fields fields) {
		this.fields = fields;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSelf() {
		return self;
	}
	public void setSelf(String self) {
		this.self = self;
	}
	
	public String getSummary() {
		return fields == null ? null : fields.summary;
	}
	
	public boolean isObalka() {
		return obalka;
	}

	public void setObalka(boolean obalka) {
		this.obalka = obalka;
	}

	public String getGuiLink() {
		return guiLink;
	}

	public void setGuiLink(String guiLink) {
		this.guiLink = guiLink;
	}
	
}
