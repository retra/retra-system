package cz.softinel.uaf.webproxycache;

import java.io.File;

class Content {

	private File content;
	private String mimeType;

	public Content(File contentFile) {
		this.content = contentFile;
		this.mimeType = null;
	}

	public boolean isValid() {
		// Check if file is valid;
		return true;
	}

	public File getContent() {
		return content;
	}

	public String getMimeType() {
		return mimeType;
	}

}
