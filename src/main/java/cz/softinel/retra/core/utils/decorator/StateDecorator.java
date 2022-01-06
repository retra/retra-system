package cz.softinel.retra.core.utils.decorator;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import cz.softinel.uaf.state.StateEntity;

public class StateDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (columnValue == null) {
			return null;
		} else {
			if (columnValue instanceof Integer) {
				Integer state = (Integer) columnValue;
				String imageName;
				String altLabelName;
				switch (state) {
				// Todo - formalize the lifecycle states in application
				case StateEntity.STATE_DELETED:
					imageName = "stateDeleted.png";
					// altLabelName = "state.deleted";
					altLabelName = "Deleted";
					break;
				case StateEntity.STATE_CLOSED:
					imageName = "stateClosed.png";
					// altLabelName = "state.active";
					altLabelName = "Closed";
					break;
				case StateEntity.STATE_ACTIVE:
				default:
					imageName = "stateActive.png";
					// altLabelName = "state.active";
					altLabelName = "Active/Open";
					break;
				}
//				String test = "<img src=\""+ pageContext.getAttribute("imgRoot")+'/'+imageName+'"'+ 
//				" alt=\"<fmt:message key='"+altLabelName+"'/>\" align=\"middle\"/>";	
				String test = "<img src=\"" + pageContext.getAttribute("imgRoot") + '/' + imageName + '"' + " alt=\""
						+ altLabelName + "\" title=\"" + altLabelName + "\" align=\"middle\"/>";
				return test;
			} else if (columnValue instanceof Boolean) {
				// HACK pincr: Used for employee flags "worklog" and "igenerate"
				final Boolean state = (Boolean) columnValue;
				final String imageName;
				final String altLabelName;
				if (state) {
					imageName = "stateActive.png";
					altLabelName = "Yes";
				} else {
					imageName = "stateClosed.png";
					altLabelName = "No";
				}
				String test = "<img src=\"" + pageContext.getAttribute("imgRoot") + '/' + imageName + '"' + " alt=\""
						+ altLabelName + "\" title=\"" + altLabelName + "\" align=\"middle\"/>";
				return test;
			} else if (columnValue instanceof String) {
				// HACK pincr: Used for user state attribute
				String state = (String) columnValue;
				String imageName;
				String altLabelName;
				if ("A".equals(state)) {
					imageName = "stateActive.png";
					altLabelName = "Active";
				} else if ("I".equals(state)) {
					imageName = "stateClosed.png";
					altLabelName = "Inactive";
				} else {
					imageName = "stateDeleted.png";
					altLabelName = "Unknown: " + state;
				}
				String test = "<img src=\"" + pageContext.getAttribute("imgRoot") + '/' + imageName + '"' + " alt=\""
						+ altLabelName + "\" title=\"" + altLabelName + "\" align=\"middle\"/>";
				return test;
			} else {
				return columnValue;
			}
		}
	}
}
