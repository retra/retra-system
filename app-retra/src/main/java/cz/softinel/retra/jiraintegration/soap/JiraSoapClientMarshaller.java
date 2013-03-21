package cz.softinel.retra.jiraintegration.soap;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.log4j.Logger;
import org.springframework.oxm.AbstractMarshaller;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

import cz.softinel.retra.jiraintegration.worklog.JiraRemoteWorklog;
import cz.softinel.retra.jiraintegration.worklog.requests.DeleteWorklogRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.DeleteWorklogResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.GetIssueRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.GetIssueResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.LoginRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.LoginResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.LogoutRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.LogoutResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogAddRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogAddResponse;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogUpdateRequest;
import cz.softinel.retra.jiraintegration.worklog.requests.WorklogUpdateResponse;

/**
 * Marshaller for the web service template.
 * @author SzalaiErik
 */
public class JiraSoapClientMarshaller extends AbstractMarshaller {
	
    private SimpleDateFormat requestDateFormat;
    private static Logger logger = Logger.getLogger(JiraSoapClientMarshaller.class);
    
    /**
     * Must use constructor for the marshaller.
     * <p>The datePattern must me set in order to function correctly. Default date format is <br/><code>yyyy-MM-dd'T'HH:mm:ss+00:00</code>
     * @param datePattern
     * @see SOAP (XML) xsd:dateTime
     */
    public JiraSoapClientMarshaller(String datePattern) {
    	requestDateFormat = new SimpleDateFormat(datePattern);
    }

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#marshalDomNode(java.lang.Object, org.w3c.dom.Node)
	 */
	protected void marshalDomNode(Object obj, Node node) {
		if (obj instanceof LoginRequest) {
			LoginRequest lr = (LoginRequest) obj;
			Element request = createElement(node, "login");
			
			Element in0 = createElement(request, "in0");
			in0.setTextContent(lr.getUsername());
			
			Element in1 = createElement(request, "in1");
			in1.setTextContent(lr.getPassword());
			
			request.appendChild(in0);
			request.appendChild(in1);
			node.appendChild(request);
		}
		else if (obj instanceof LogoutRequest) {
			LogoutRequest lr = (LogoutRequest) obj;
			
			Element request = createElement(node, "logout");
			
			Element in0 = createElement(request, "in0");
			in0.setTextContent(lr.getLoginToken());
			
			request.appendChild(in0);
			node.appendChild(request);
		}
		else if (obj instanceof WorklogAddRequest) {
			WorklogAddRequest war = (WorklogAddRequest) obj;
			Element request = createElement(node, "addWorklogAndAutoAdjustRemainingEstimate");
			
			Element in0 = createElement(request,"in0");
			in0.setTextContent(war.getLoginToken());
			
			Element in1 = createElement(request, "in1");
			in1.setTextContent(war.getJiraIssue());
			
			Element in2 = createElement(request, "in2");
			createWorklogElement(in2, war.getJiraRemoteWorklog());
			
			request.appendChild(in0);
			request.appendChild(in1);
			request.appendChild(in2);
			node.appendChild(request);

		} else if (obj instanceof WorklogUpdateRequest) {
			WorklogUpdateRequest war = (WorklogUpdateRequest) obj;
			Element request = createElement(node, "updateWorklogAndAutoAdjustRemainingEstimate");
			
			Element in0 = createElement(request,"in0");
			in0.setTextContent(war.getLoginToken());
			
			Element in1 = createElement(request, "in1");
			createWorklogElement(in1, war.getJiraRemoteWorklog());
			
			request.appendChild(in0);
			request.appendChild(in1);
			node.appendChild(request);
		} else if (obj instanceof GetIssueRequest) {
			GetIssueRequest getIssue = (GetIssueRequest) obj;
			Element request = createElement(node, "getIssue");
			
			Element in0 = createElement(request, "in0");
			in0.setTextContent(getIssue.getToken());
			
			Element in1 = createElement(request, "in1");
			in1.setTextContent(getIssue.getIssue());
			
			request.appendChild(in0);
			request.appendChild(in1);
			node.appendChild(request);
		} else if (obj instanceof DeleteWorklogRequest) {
			DeleteWorklogRequest deleteWorklog = (DeleteWorklogRequest) obj;
			Element request = createElement(node, "deleteWorklogAndAutoAdjustRemainingEstimate");
			
			Element in0 = createElement(request, "in0");
			in0.setTextContent(deleteWorklog.getLoginToken());
			
			Element in1 = createElement(request, "in1");
			in1.setTextContent(deleteWorklog.getIssueId());
			
			request.appendChild(in0);
			request.appendChild(in1);
			node.appendChild(request);
		}
//		MarshallerHelper.printNode(node, "");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#unmarshalDomNode(org.w3c.dom.Node)
	 */
	protected Object unmarshalDomNode(Node node) {
		String text = node.getNodeName();
		if (text.equals("loginResponse")) {
			LoginResponse response = new LoginResponse();
			response.setToken(node.getTextContent());
			return response;
		}
		else if (text.equals("logoutResponse")) {
			LogoutResponse response = new LogoutResponse();
			response.setSuccess(node.getTextContent());
			return response;
		}
		else if (text.equals("addWorklogAndAutoAdjustRemainingEstimateResponse")) {
			WorklogAddResponse response = new WorklogAddResponse();
			Node remoteWorklogResult = node.getNextSibling();
			if (remoteWorklogResult != null ) {
				Node id = MarshallerHelper.getSiblingByName(remoteWorklogResult.getFirstChild(), "id");
				if(id != null) {
					response.setSuccess(id.getTextContent());
				} else {
					logger.error("No id found in response!");
				}
			} else {
				logger.error("No worklog response found in SOAP!");
			}
			return response;
		} else if (text.equals("updateWorklogAndAutoAdjustRemainingEstimateResponse")) {
			WorklogUpdateResponse response = new WorklogUpdateResponse();
			return response;
		} else if (text.equals("getIssueResponse")) {
			GetIssueResponse response = new GetIssueResponse();
			response.setAssigne(node.getTextContent());
			return response;
		} else if (text.equals("deleteWorklogAndAutoAdjustRemainingEstimateResponse")) {
			DeleteWorklogResponse response = new DeleteWorklogResponse();
			return response;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.oxm.Marshaller#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class klass) {
		if (klass.equals(LoginRequest.class)) {
			return true;
		} else if (klass.equals(LoginResponse.class)) {
			return true;
		}
		return false;
	}
	
	private Element createElement(Node parent, String element) {
		return parent.getOwnerDocument().createElement(element);
	}
	
	private void createWorklogElement(Element remoteWorklogElement, JiraRemoteWorklog remoteWorklog) {
		Element author 				= createElement(remoteWorklogElement, "author");
		Element comment 			= createElement(remoteWorklogElement, "comment");
//		Element created 			= createElement(remoteWorklogElement, "created");
		createElement(remoteWorklogElement, "created");
//		Element groupLevel 			= createElement(remoteWorklogElement, "groupLevel");
		createElement(remoteWorklogElement, "groupLevel");
		Element id 					= createElement(remoteWorklogElement, "id");
//		Element roleLevelId   	  	= createElement(remoteWorklogElement, "roleLevelId");
		createElement(remoteWorklogElement, "roleLevelId");
		Element startDate 			= createElement(remoteWorklogElement, "startDate"); // date
		Element timeSpent 			= createElement(remoteWorklogElement, "timeSpent");
		Element timeSpentInSeconds 	= createElement(remoteWorklogElement, "timeSpentInSeconds"); // long
//		Element updateAuthor   	  	= createElement(remoteWorklogElement, "updateAuthor");
		createElement(remoteWorklogElement, "updateAuthor");
//		Element updated 			= createElement(remoteWorklogElement, "updated"); // date
		createElement(remoteWorklogElement, "updated"); // date

		author.setTextContent(remoteWorklog.getAuthor());
		comment.setTextContent(remoteWorklog.getComment());
		startDate.setTextContent(requestDateFormat.format(remoteWorklog.getCreated()));
		
		timeSpent.setTextContent(remoteWorklog.getTimeSpent());
//		timeSpentInSeconds.setTextContent(Long.toString(war.getJiraRemoteWorklog().getTimeSpentInSeconds()));
		timeSpentInSeconds.setTextContent(Long.toString(remoteWorklog.getTimeSpentInSeconds()));
		id.setTextContent(remoteWorklog.getId());
		
		remoteWorklogElement.appendChild(author);
		remoteWorklogElement.appendChild(comment);
//		in1.appendChild(created);
		//in2.appendChild(groupLevel);
		remoteWorklogElement.appendChild(id);
		//in2.appendChild(roleLevelId);
		remoteWorklogElement.appendChild(startDate);
		remoteWorklogElement.appendChild(timeSpent);
		remoteWorklogElement.appendChild(timeSpentInSeconds);
		//in2.appendChild(updateAuthor);
		//in2.appendChild(updated);
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#marshalOutputStream(java.lang.Object, java.io.OutputStream)
	 */
	protected void marshalOutputStream(Object arg0, OutputStream arg1) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#marshalSaxHandlers(java.lang.Object, org.xml.sax.ContentHandler, org.xml.sax.ext.LexicalHandler)
	 */
	protected void marshalSaxHandlers(Object arg0, ContentHandler arg1, LexicalHandler arg2) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#marshalWriter(java.lang.Object, java.io.Writer)
	 */
	protected void marshalWriter(Object arg0, Writer arg1) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#marshalXmlEventWriter(java.lang.Object, javax.xml.stream.XMLEventWriter)
	 */
	protected void marshalXmlEventWriter(Object arg0, XMLEventWriter arg1) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#marshalXmlStreamWriter(java.lang.Object, javax.xml.stream.XMLStreamWriter)
	 */
	protected void marshalXmlStreamWriter(Object arg0, XMLStreamWriter arg1) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}


	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#unmarshalInputStream(java.io.InputStream)
	 */
	protected Object unmarshalInputStream(InputStream arg0) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#unmarshalReader(java.io.Reader)
	 */
	protected Object unmarshalReader(Reader arg0) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#unmarshalSaxReader(org.xml.sax.XMLReader, org.xml.sax.InputSource)
	 */
	protected Object unmarshalSaxReader(XMLReader arg0, InputSource arg1) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#unmarshalXmlEventReader(javax.xml.stream.XMLEventReader)
	 */
	protected Object unmarshalXmlEventReader(XMLEventReader arg0) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.oxm.AbstractMarshaller#unmarshalXmlStreamReader(javax.xml.stream.XMLStreamReader)
	 */
	protected Object unmarshalXmlStreamReader(XMLStreamReader arg0) {
		throw new UnsupportedOperationException("This method is not supported by this implementation.");
	}
}
