package cz.softinel.retra.core.utils.decorator;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

import cz.softinel.uaf.state.StateEntity;

public class StateDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media)
			throws DecoratorException {
		if (columnValue == null) {
			return null;			
		} else {
			if (columnValue instanceof Integer) {
				Integer state = (Integer) columnValue;
				String imageName;
				String altLabelName;
				switch (state) {
				//Todo - formalize the lifecycle states in application
				case StateEntity.STATE_DELETED:
					imageName = "stateDeleted.png";
					//altLabelName = "state.deleted";
					altLabelName = "Deleted";
					break;					
				case StateEntity.STATE_CLOSED:
					imageName = "stateClosed.png";
					//altLabelName = "state.active";
					altLabelName = "Closed";
					break;
				case StateEntity.STATE_ACTIVE:
				default:
					imageName = "stateActive.png";
					//altLabelName = "state.active";
					altLabelName = "Active/Open";
					break;
				}		
//				String test = "<img src=\""+ pageContext.getAttribute("imgRoot")+'/'+imageName+'"'+ 
//				" alt=\"<fmt:message key='"+altLabelName+"'/>\" align=\"middle\"/>";	
				String test = "<img src=\""+ pageContext.getAttribute("imgRoot")+'/'+imageName+'"'+ 
				" alt=\""+altLabelName+"\" title=\""+altLabelName+"\" align=\"middle\"/>";				
				return test;	
			} else {
				return columnValue;
			}
		}		
	}
}
