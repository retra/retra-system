package cz.softinel.retra.core.code;

import java.text.DecimalFormat;

/**
 * Helper class for code generate
 * 
 * @author petr
 *
 */
public abstract class CodeGenerator {

	public static String generateNewCode(final DecimalFormat formatter, final int number) {
		String output = formatter.format(number);
		return output;
	}

	public static String generateNewCode(final String pattern, final int number) {
		DecimalFormat formatter = new DecimalFormat(pattern);
		String output = formatter.format(number);
		return output;
	}

}
