package cz.softinel.retra.component.blo;

import java.util.List;

import cz.softinel.retra.component.Component;

/**
 * this class represents all logic for components.
 *
 * @author Zoltan Vadasz
 */
public interface ComponentLogic {

	/**
	 * Find all components items.
	 * 
	 * @return
	 */
	public List<Component> findAllComponents();

	/**
	 * Find all component items, which are not deleted.
	 * 
	 * @return
	 */
	public List<Component> findAllNotDeletedComponents();

	public List<Component> findComponentsWithCode(String code);

	public Component create(Component component);

	public Component get(Long pk);

	public void store(Component component);
}
