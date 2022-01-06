package cz.softinel.retra.type.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.core.blo.AbstractLogicBean;
import cz.softinel.retra.type.Type;
import cz.softinel.retra.type.dao.TypeDao;

/**
 * Implementation of activity logic
 * 
 * @version $Revision: 1.3 $ $Date: 2007-02-23 12:16:27 $
 * @author Petr Sígl
 */
public class TypeLogicImpl extends AbstractLogicBean implements TypeLogic {

	private TypeDao typeDao;

	/**
	 * @return the typeDao
	 */
	public TypeDao getTypeDao() {
		return typeDao;
	}

	/**
	 * @param typeDao the typeDao to set
	 */
	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
	}

	/**
	 * @see cz.softinel.retra.type.blo.TypeLogic#findAllTypes()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Type> findAllTypes() {
		return typeDao.selectAll();
	}
}
