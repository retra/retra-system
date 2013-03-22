package cz.softinel.uaf.webproxycache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cz.softinel.uaf.util.EncryptionHelper;
import cz.softinel.uaf.util.StreamHelper;

public final class WebProxyCache {

	private final Log logger = LogFactory.getLog(this.getClass());

	private static WebProxyCache instance;
	
	private final Map<String, Content> cache;
	
	private WebProxyCache() {
		cache = new HashMap<String, Content>();
	}
	
	public static WebProxyCache getInstance() {
		if (instance == null) {
			instance = new WebProxyCache();
		}
		return instance;
	}
	
	public String getProxyUrl(String url) {
		// TODO radek: Use some spring configuration ...
		String proxyUrl = "WebProxyCache?url=" + EncryptionHelper.urlEncode(url); 
		logger.debug("Using proxy URL for: " + url + " -> " + proxyUrl);
		return proxyUrl;
	}
	
	public Content getContent(String url) {
		Content content = cache.get(url);
		if (content == null || ! content.isValid()) {
			content = loadContent(url);
			cache.put(url, content);
		}
		return content;
	}
	
	public Content loadContent(String urlString) {
		try {
			URL url = new URL(urlString);
			InputStream is = url.openStream();
			if (is == null) {
				throw new RuntimeException("Cannot open url stream (null).");
			}
			File contentFile = makeCacheFile(urlString);
			OutputStream os = new FileOutputStream(contentFile);
			StreamHelper.copy(is, os);
			os.close();
			return new Content(contentFile);
		} catch (MalformedURLException e) {
			throw new RuntimeException("An error occured during getting content: " + e.getMessage(), e); 
		} catch (IOException e) {
			throw new RuntimeException("An error occured during getting content: " + e.getMessage(), e); 
		}
		
	}
	
	private File makeCacheFile(String url) throws IOException {
		File temp = File.createTempFile("WebProxyCache", "." + getFileName(url));
		temp.deleteOnExit();
		return temp;
	}
	
	private String getFileName(String urlString) throws MalformedURLException {
		URL url = new URL(urlString);
		String path = url.getPath();
		String parts[] = path.split("/");
		return parts[parts.length-1];
	}
	
}
