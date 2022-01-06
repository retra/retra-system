package cz.softinel.retra.core.utils.decorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import cz.softinel.retra.core.utils.TypeFormats;

public class HourDecorator implements DisplaytagColumnDecorator {

	private DateFormat dateFormat;

	public HourDecorator() {
		dateFormat = new SimpleDateFormat(TypeFormats.HOUR_FORMAT);
	}

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (columnValue instanceof Date) {
			return dateFormat.format(columnValue);
		}

		// not date return it as it comes
		return columnValue;
	}
}
