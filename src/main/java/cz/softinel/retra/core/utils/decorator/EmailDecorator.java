package cz.softinel.retra.core.utils.decorator;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class EmailDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (columnValue == null) {
			return null;
		}
		String escaped = StringEscapeUtils.escapeHtml(columnValue.toString());
		return "<a href='mailto:" + escaped + "'>" + escaped + "</a>";
	}

}
