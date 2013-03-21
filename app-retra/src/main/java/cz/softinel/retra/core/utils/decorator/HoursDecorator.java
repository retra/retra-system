package cz.softinel.retra.core.utils.decorator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.springframework.web.servlet.support.RequestContextUtils;

import cz.softinel.retra.core.utils.TypeFormats;

/**
 * Displaytag decorator for displaying period in hours.
 *
 * @version $Revision: 1.2 $ $Date: 2007-02-18 17:38:25 $
 * @author Pavel Mueller
 */
public class HoursDecorator implements DisplaytagColumnDecorator {
	
	/**
	 * @see org.displaytag.decorator.DisplaytagColumnDecorator#decorate(java.lang.Object, javax.servlet.jsp.PageContext, org.displaytag.properties.MediaTypeEnum)
	 */
	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (columnValue instanceof Number) {
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			Locale currentLocale = RequestContextUtils.getLocale(request);
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(currentLocale);
			dfs.setDecimalSeparator('.');
			NumberFormat formatter = new DecimalFormat(TypeFormats.HOURS_FORMAT, dfs);
			return formatter.format(columnValue);
		}

		// not number return it as it comes
		return columnValue;
	}
}
