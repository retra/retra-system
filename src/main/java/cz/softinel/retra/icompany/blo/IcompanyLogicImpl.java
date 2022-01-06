package cz.softinel.retra.icompany.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.icompany.Icompany;
import cz.softinel.retra.icompany.dao.IcompanyDao;

/**
 * Implementation of icompany logic
 * 
 * @author Petr Sígl
 */
public class IcompanyLogicImpl extends AbstractLogicBean implements IcompanyLogic {

	private IcompanyDao icompanyDao;

	/**
	 * @return the icompanyDao
	 */
	public IcompanyDao getIcompanyDao() {
		return icompanyDao;
	}

	/**
	 * @param icompanyDao the icompanyDao to set
	 */
	public void setIcompanyDao(IcompanyDao icompanyDao) {
		this.icompanyDao = icompanyDao;
	}

	/**
	 * @see cz.softinel.retra.icompany.blo.IcompanyLogic#findAllIcompanies()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Icompany> findAllIcompanies() {
		return icompanyDao.selectAll();
	}

	/**
	 * @see cz.softinel.retra.icompany.blo.IcompanyLogic#findAllNotDeletedIcompanies()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Icompany> findAllNotDeletedIcompanies() {
		return icompanyDao.selectAllNotDeleted();
	}
}
