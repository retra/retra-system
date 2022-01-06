package cz.softinel.retra.component.web;

/**
 * @author petr
 *
 */
public class ComponentForm {

	private String pk;

	private String name;
	private String code;
	private String projectPk;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getProjectPk() {
		return projectPk;
	}

	public void setProjectPk(String projectPk) {
		this.projectPk = projectPk;
	}

	@Override
	public String toString() {
		return "(ComponentForm: pk=" + pk + ", name=" + name + ", code=" + code + ", projectPk=" + projectPk + ")";
	}
	
}
