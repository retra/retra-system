package cz.softinel.retra.core.utils.decorator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import cz.softinel.retra.schedule.Schedule;
import cz.softinel.retra.type.Type;

public class ScheduleOverviewDecorator implements DisplaytagColumnDecorator {

	private DateFormat dateFormat;
	private HourDecorator hourDecorator;

	public ScheduleOverviewDecorator() {
		dateFormat = new SimpleDateFormat("d");
		hourDecorator = new HourDecorator();
	}

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		Object result = columnValue;
		if (columnValue != null && columnValue instanceof Schedule) {
			Schedule schedule = (Schedule) columnValue;
			Date date = schedule.getWorkFrom();
			String dateStr = "";
			if (date != null) {
				dateStr = dateFormat.format(date);
			}
			String hourFrom = (String) hourDecorator.decorate(schedule.getWorkFrom(), pageContext, media);
			String hourTo = (String) hourDecorator.decorate(schedule.getWorkTo(), pageContext, media);
			String sign = "-";
			if (hourFrom == null || hourTo == null) {
				hourFrom = "";
				hourTo = "";
				sign = "";
			}
			Type type = schedule.getType();
			String typeName = "";
			if (type != null) {
				typeName = type.getName();
			}
			String comment = schedule.getComment();
			if (comment == null) {
				comment = "";
			}
			Long pk = schedule.getPk();

			if (media == MediaTypeEnum.HTML) {
				StringBuffer sb = new StringBuffer("<div class=\"right\">");
				sb.append(dateStr);
				sb.append("<br />");
				sb.append("<div class=\"center\">");
				sb.append("<span class=\"visibleOnlyInPrint\">");
				sb.append(typeName);
				sb.append("<br />");
				sb.append("</span>");
				sb.append("<a href=\"ScheduleView.do?fkprm=true&schedulePk=");
				sb.append(pk);
				sb.append("\" title=\"");
				sb.append(comment);
				sb.append("\">");
				sb.append(hourFrom);
				sb.append("<br />");
				sb.append(sign);
				sb.append("<br />");
				sb.append(hourTo);
				sb.append("</a>");
				sb.append("<br />");
				sb.append("</div>");
				sb.append("</div>");

				result = sb.toString();
			} else {
				StringBuffer sb = new StringBuffer("");
				sb.append(dateStr);
				sb.append(" \n");
				sb.append(typeName);
				sb.append(" \n");
				sb.append(hourFrom);
				sb.append(" \n");
				sb.append(sign);
				sb.append(" \n");
				sb.append(hourTo);
				sb.append(" \n");
				sb.append(comment);
				sb.append(" \n");

				result = sb.toString();
			}

		}

		// not schedule return it as it comes
		return result;
	}
}
