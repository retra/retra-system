package cz.softinel.retra.schedule.web;

/**
 * This class is used for binding workFrom jsp workTo entity.
 * 
 * @version $Revision: 1.1 $ $Date: 2007-01-29 07:11:43 $
 * @author Petr Sígl
 */
public class ScheduleForm {

	private String pk;
	private String date;
	private String workFrom;
	private String workTo;
	private String comment;
	private String type;
	private String copyType;
	private String copyDestinationFrom;
	private String copyDestinationTo;

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the description
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param description the description to set
	 */
	public void setComment(String description) {
		this.comment = description;
	}

	/**
	 * @return the pk
	 */
	public String getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(String pk) {
		this.pk = pk;
	}

	/**
	 * @return the project
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param project the project to set
	 */
	public void setType(String project) {
		this.type = project;
	}

	/**
	 * @return the workFrom
	 */
	public String getWorkFrom() {
		return workFrom;
	}

	/**
	 * @param workFrom the workFrom to set
	 */
	public void setWorkFrom(String workFrom) {
		this.workFrom = workFrom;
	}

	/**
	 * @return the workTo
	 */
	public String getWorkTo() {
		return workTo;
	}

	/**
	 * @param workTo the workTo to set
	 */
	public void setWorkTo(String workTo) {
		this.workTo = workTo;
	}

	/**
	 * @return the copyType
	 */
	public String getCopyType() {
		return copyType;
	}

	/**
	 * @param copyType the copyType to set
	 */
	public void setCopyType(String copyType) {
		this.copyType = copyType;
	}

	/**
	 * @return the copyDestinationFrom
	 */
	public String getCopyDestinationFrom() {
		return copyDestinationFrom;
	}

	/**
	 * @param copyDestinationFrom the copyDestinationFrom to set
	 */
	public void setCopyDestinationFrom(String copyDestinationFrom) {
		this.copyDestinationFrom = copyDestinationFrom;
	}

	/**
	 * @return the copyDestinationTo
	 */
	public String getCopyDestinationTo() {
		return copyDestinationTo;
	}

	/**
	 * @param copyDestinationTo the copyDestinationTo to set
	 */
	public void setCopyDestinationTo(String copyDestinationTo) {
		this.copyDestinationTo = copyDestinationTo;
	}

	@Override
	public String toString() {
		return "(ScheduleForm: pk=" + pk + ", date=" + date + ", workFrom=" + workFrom + ", workTo=" + workTo
				+ ", comment=" + comment + ", type=" + type + ", copyType=" + copyType + ", copyDestinationFrom="
				+ copyDestinationFrom + ", copyDestinationTo=" + copyDestinationTo + ")";
	}
	
	
}
