package cz.softinel.retra.invoiceseq.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.Assert;

import cz.softinel.retra.invoiceseq.InvoiceSeq;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

public class HibernateInvoiceSeqDao extends AbstractHibernateDao implements InvoiceSeqDao {

	public InvoiceSeq get(Long pk) {
		// check if invoice id is defined
		Assert.notNull(pk);
		InvoiceSeq invoiceSeq = (InvoiceSeq) getHibernateTemplate().get(InvoiceSeq.class, pk);
		return invoiceSeq;
	}

	public InvoiceSeq getForNextNumber(Long pk) {
		// check if invoice id is defined
		Assert.notNull(pk);
		InvoiceSeq invoiceSeq = (InvoiceSeq) getHibernateTemplate().get(InvoiceSeq.class, pk, LockMode.UPGRADE);
		return invoiceSeq;
	}

	public void update(InvoiceSeq invoiceSeq) {
		getHibernateTemplate().update(invoiceSeq);
	}

	@SuppressWarnings("unchecked")
	public List<InvoiceSeq> selectAllActive() {
		Object[] states = new Object[] { InvoiceSeq.STATE_ACTIVE };

		Session session = getSession();
		Query query = session.getNamedQuery("InvoiceSeq.selectAllWithStates");
		query.setParameterList("states", states);

		try {
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

}