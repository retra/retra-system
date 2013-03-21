package cz.softinel.uaf.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptionHelper {

	private static final String MD5_HASH_ALGORITHM = "MD5";
	private static final String URL_ENCODING = "UTF-8";
	
	private EncryptionHelper() {}
	
	public static String md5(String text) {
		if (text == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance(MD5_HASH_ALGORITHM);
	    	byte digest[] = md.digest(text.getBytes());
	    	return ArrayHelper.toHexString(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String urlEncode(String url) {
		try {
			return 	URLEncoder.encode(url, URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unexpected exception: " + e.getMessage(), e);
		}
	}
}
