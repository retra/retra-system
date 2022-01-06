package cz.softinel.retra.worklog.web;

/**
 * Activity as represented in a worklog being imported.
 * 
 * <p>
 * Activity ID or name can be generated if missing
 *
 * @version $Revision: 1.2 $ $Date: 2007-11-25 22:51:59 $
 * @author Pavel Mueller
 * @see WorklogImportForm
 * @see ImportDataParser
 */
public class ExternalActivity {
	private String id;
	private String name;
	private Long activityId;

	/**
	 * Constructs activity
	 * 
	 * @param id   activity is
	 * @param name activity name
	 */
	public ExternalActivity(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Returns activity id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns activity name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns Mira activity pk mapped to this imported activity
	 * 
	 * @return the activityId
	 */
	public Long getActivityId() {
		return activityId;
	}

	/**
	 * Sets Mira activity pk mapped to this imported activity
	 * 
	 * @param activityId the activityId to set
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof ExternalActivity)) {
			return false;
		}

		ExternalActivity that = (ExternalActivity) obj;
		return that.id.equals(this.id);
	}
}