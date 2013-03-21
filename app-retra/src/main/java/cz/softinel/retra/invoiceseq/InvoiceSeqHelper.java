package cz.softinel.retra.invoiceseq;

public class InvoiceSeqHelper {

	public static String getCodeAndName(InvoiceSeq invoiceSeq) {
		StringBuffer sb = new StringBuffer();
		sb.append(invoiceSeq.getCode());
		sb.append(" - ");
		sb.append(invoiceSeq.getName());
		return sb.toString().trim();
	}

}
