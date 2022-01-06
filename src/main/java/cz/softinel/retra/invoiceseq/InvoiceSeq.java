package cz.softinel.retra.invoiceseq;

import cz.softinel.uaf.state.StateEntity;

/**
 * This class represents invoice sequence.
 * 
 * @version $Revision: 1.5 $ $Date: 2007-04-03 10:41:00 $
 * @author Petr Sígl
 */
public class InvoiceSeq implements StateEntity {

	// attributes
	private Long pk;

	private String code;
	private String name;
	private String pattern;

	private int sequence;

	private int state;

	private int step;

	// Business fields ...

	public String getCodeAndName() {
		return InvoiceSeqHelper.getCodeAndName(this);
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

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (this.pk == null) {
			return super.hashCode();
		}
		return this.pk.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof InvoiceSeq)) {
			return false;
		}
		InvoiceSeq that = (InvoiceSeq) obj;
		if (this.pk == null || that.pk == null) {
			return false;
		}
		return this.pk.equals(that.pk);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(InvoiceSeq: pk=" + pk + ", code=" + code + ", name=" + name + ")";
	}

}
