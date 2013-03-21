package cz.softinel.retra.type;



/**
 * This class represents type for schedule (default, holidays etc.).
 *
 * @version $Revision: 1.3 $ $Date: 2007-02-08 20:23:28 $
 * @author Petr Sígl
 */
public class Type {
	
	//attributes
	private Long pk;
	
	private String code;
	private String name;
	private String description;

	// Business fields ...

	public String getCodeAndName() {
		return TypeHelper.getCodeAndName(this);
	}
	
	// Getter and Setters ...
	
	/**
	 * @return the pk
	 */
	public Long getPk() {
		return pk;
	}

	/**
	 * @param pk the pk to set
	 */
	public void setPk(Long pk) {
		this.pk = pk;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	//bussiness and gui methods
	
	/**
	 * This method returns cssClass (for gui) 
	 * @return the css class
	 */
	//TODO: maybe better move somewhere else, but question is where
	public String getCssClass(){
		return TypeHelper.getCssClass(this);
	}
}
