package cz.softinel.retra.invoice.web;

public class InvoiceForm extends AbstractInvoiceForm {

	private String pk;

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getPk() {
		return pk;
	}

	@Override
	public String toString() {
		return super.toString()+"=>(InvoiceForm: pk=" + pk + ")";
	}
	
}
