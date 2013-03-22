package cz.softinel.uaf.lovs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents all list of values in system.
 *
 * @version $Revision: 1.1 $ $Date: 2007-01-29 07:11:43 $
 * @author Petr Sígl
 */
@XmlRootElement(name="lovs")
public class Lovs {
	private List<Lov> lovs = new ArrayList<Lov>();

	/**
	 * @return the lovs
	 */
	@XmlElement(name="lov")
	public List<Lov> getLovs() {
		return lovs;
	}

	/**
	 * @param lovs the lovs to set
	 */
	public void setLovs(List<Lov> lovs) {
		this.lovs = lovs;
	}
	
	public void addLov(Lov lov) {
		lovs.add(lov);
	}
	
	public void addLovs(List<Lov> lovs) {
		this.lovs.addAll(lovs);
	}
}