package cz.softinel.retra.invoice.web;

public class AbstractInvoiceForm {

	private String orderDate;
	private String startDate;
	private String finishDate;

	private String name;
	private String code;

	private String sequence;

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "(AbstractInvoiceForm [orderDate=" + orderDate + ", startDate=" + startDate +", finishDate=" + finishDate + ", name=" + name
				+ ", code=" + code + ", sequence=" + sequence + ")";
	}
	
}
