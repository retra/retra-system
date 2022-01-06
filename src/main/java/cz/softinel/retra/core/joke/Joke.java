package cz.softinel.retra.core.joke;

public class Joke {

	private String html;
	private String label;

	public Joke() {
		this.html = null;
		this.label = null;
	}

	public Joke(String html, String label) {
		this.html = html;
		this.label = label;
	}

	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}

	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "(Joke: label=" + label + ")";
	}
	
}
