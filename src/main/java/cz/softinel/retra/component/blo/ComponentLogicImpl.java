package cz.softinel.retra.component.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.component.Component;
import cz.softinel.retra.component.dao.ComponentDao;
import cz.softinel.retra.core.blo.AbstractLogicBean;

/**
 * Implementation of component logic
 * 
 * @author Zoltan Vadasz
 */
public class ComponentLogicImpl extends AbstractLogicBean implements ComponentLogic {

	private ComponentDao componentDao;

	/**
	 * @return the componentDao
	 */
	public ComponentDao getComponentDao() {
		return componentDao;
	}

	/**
	 * @param componentDao the componentDao to set
	 */
	public void setComponentDao(ComponentDao componentDao) {
		this.componentDao = componentDao;
	}

	/**
	 * @see cz.softinel.retra.component.blo.ComponentLogic#findAllComponents()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Component> findAllComponents() {
		return componentDao.selectAll();
	}

	/**
	 * @see cz.softinel.retra.component.blo.ComponentLogic#findAllNotDeletedComponents()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Component> findAllNotDeletedComponents() {
		return componentDao.selectAllNotDeleted();
	}

	public Component create(Component component) {
		return componentDao.insert(component);
	}

	public List<Component> findComponentsWithCode(String code) {
		return componentDao.selectComponentsWithCode(code);
	}

	public Component get(Long pk) {
		return componentDao.get(pk);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void store(Component component) {
		componentDao.update(component);
	}
}
