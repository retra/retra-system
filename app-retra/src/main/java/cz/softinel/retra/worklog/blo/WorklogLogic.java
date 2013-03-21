package cz.softinel.retra.worklog.blo;

import java.util.List;

import cz.softinel.retra.invoice.Invoice;
import cz.softinel.retra.worklog.Worklog;
import cz.softinel.retra.worklog.WorklogViewOverview;
import cz.softinel.uaf.filter.Filter;

/**
 * This class represents all logic for worklogs.
 *
 * @version $Revision: 1.11 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public interface WorklogLogic {
	
	/**
	 * Find all worklog items.
	 * 
	 * @return
	 */
	public List<Worklog> findAllWorklogs();
	
	/**
	 * Find all worklog items for employee identified by pk.
	 * 
	 * @param pk of user
	 * @return
	 */
	public List<Worklog> findAllWorklogsForEmployee(Long pk);

	/**
	 * Find all worklog items for invoice identified by pk.
	 * 
	 * @param pk of invoice
	 * @return
	 */
	public List<Worklog> findAllWorklogsForInvoice(Long pk);
	
	/**
	 * Find worklogs according to given filter parameters  
	 * 
	 * @param filter
	 * @return
	 */
	public List<Worklog> findByFilter(Filter filter);

	/**
	 * Creates worklog
	 * 
	 * @param worklog
	 * @return
	 */
	public Worklog create(Worklog worklog);
	
	/**
	 * Creates all worklog items given in the list
	 * @param worklogItems worklogs to create
	 * @param messages
	 */
	public int create(List<Worklog> worklogItems);

	/**
	 * Remove given worklog
	 * 
	 * @param worklog
	 */
	public void remove(Worklog worklog);
	
	/**
	 * Remove worklog with given pk
	 * 
	 * @param pk
	 */
	public void remove(Long pk);
	
	/**
	 * Get worklog according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public Worklog get(Long pk);
	
	/**
	 * Get worklog according to given pk.
	 * 
	 * @param pk
	 * @return
	 */
	public void loadAndLoadLazy(Worklog worklog);
	
	/**
	 * Stores given worklog
	 * 
	 * @param worklog
	 */
	public void store(Worklog worklog);
	
	/**
	 * Find worklog overviews according to given filter parameters
	 * 
	 * @param filter
	 * @param messages
	 * @return
	 */
	public List<WorklogViewOverview> findWorklogOverviewByFilter(Filter filter);

	/**
	 * Unpair worklog with invoice.
	 * 
	 * @param worklog
	 * @param invoice
	 */
	public void unpairWorklogWithInvoice(Worklog worklog, Invoice invoice); 

	/**
	 * Pair worklog with invoice.
	 * 
	 * @param worklog
	 * @param invoice
	 */
	public void pairWorklogWithInvoice(Worklog worklog, Invoice invoice); 

	/**
	 * Unpair worklog with invoice.
	 * 
	 * @param invoicePk
	 * @param worklogsPk
	 */
	public int pairWorklogWithInvoice(Long invoicePk, Long... worklogsPk);

}
