package cz.softinel.retra.worklog.web;

/**
 * Project as represented in a worklog being imported.
 * 
 * <p>Project ID or name can be generated if missing
 *
 * @version $Revision: 1.2 $ $Date: 2007-11-25 22:51:59 $
 * @author Pavel Mueller
 * @see WorklogImportForm
 * @see ImportDataParser
 */
public class ExternalProject {

	/** External project ID */
	private final String id;
	
	/** External project name */
	private final String name;
	
	/** Internal project ID */
	private Long projectId;
	
	/** Internal invoice ID */
	private Long invoiceId;
	
	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * Constructs project
	 * @param id project id
	 * @param name project name
	 */
	public ExternalProject(final String id, final String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Returns project id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Returns project name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns Mira project id mapped to this imported project
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}
	
	/**
	 * Sets Mira project id mapped to this imported project
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
		if (!(obj instanceof ExternalProject)) {
			return false;
		}
		final ExternalProject that = (ExternalProject) obj;
		return that.id.equals(this.id);
	}
}