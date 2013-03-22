package cz.softinel.retra.core.code;

import java.text.DecimalFormat;

import org.junit.Test;
import junit.framework.Assert;

public class CodeGeneratorTest extends Assert {

	private DecimalFormat formatter = new DecimalFormat("'2012V'0000");
	
	@Test
	public void TestGenerateNewCodeStringOne() {
		String code = CodeGenerator.generateNewCode("'2012V'0000", 9);
		Assert.assertEquals("2012V0009", code);
	}

	@Test
	public void TestGenerateNewCodeStringTwo() {
		String code = CodeGenerator.generateNewCode("'2012V'0000", 99);
		Assert.assertEquals("2012V0099", code);
	}

	@Test
	public void TestGenerateNewCodeStringThree() {
		String code = CodeGenerator.generateNewCode("'2012V'0000", 999);
		Assert.assertEquals("2012V0999", code);
	}
	
	@Test
	public void TestGenerateNewCodeStringFour() {
		String code = CodeGenerator.generateNewCode("'2012V'0000", 9999);
		Assert.assertEquals("2012V9999", code);
	}

	@Test
	public void TestGenerateNewCodeStringFive() {
		String code = CodeGenerator.generateNewCode("'2012V'0000", 99999);
		Assert.assertEquals("2012V99999", code);
	}

	@Test
	public void TestGenerateNewCodeFormatterOne() {
		String code = CodeGenerator.generateNewCode(formatter, 9);
		Assert.assertEquals("2012V0009", code);
	}

	@Test
	public void TestGenerateNewCodeFormatterTwo() {
		String code = CodeGenerator.generateNewCode(formatter, 99);
		Assert.assertEquals("2012V0099", code);
	}

	@Test
	public void TestGenerateNewCodeFormatterThree() {
		String code = CodeGenerator.generateNewCode(formatter, 999);
		Assert.assertEquals("2012V0999", code);
	}
	
	@Test
	public void TestGenerateNewCodeFormatterFour() {
		String code = CodeGenerator.generateNewCode(formatter, 9999);
		Assert.assertEquals("2012V9999", code);
	}

	@Test
	public void TestGenerateNewCodeFormatterFive() {
		String code = CodeGenerator.generateNewCode(formatter, 99999);
		Assert.assertEquals("2012V99999", code);
	}

	
}
