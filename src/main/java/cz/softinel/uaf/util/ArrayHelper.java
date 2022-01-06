package cz.softinel.uaf.util;

public class ArrayHelper {

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * Convert sequence of bytes to hex string with lowercase characters. TODO:
	 * Refactor, use common utils
	 * 
	 * @param bytes
	 * @return Converted string, will use lowercase characters
	 */
	public static String toHexString(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			buf.append(HEX_CHAR[(bytes[i] & 0xf0) >>> 4]);
			buf.append(HEX_CHAR[bytes[i] & 0x0f]);
		}
		return buf.toString();
	}

}
