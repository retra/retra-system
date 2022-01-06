package cz.softinel.retra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class ISO8601ParserTest {

	@Test
	public void test() {

		// 2016-02-07T11:00:00.000+0100

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		Date now = new Date();

		String formated = df.format(now);

		System.out.println(formated);
	}

}
