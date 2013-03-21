package cz.softinel.retra.activity.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import cz.softinel.retra.activity.Activity;

/**
 * This class is hibernate dependent implementation of ActivityDao  
 *
 * @version $Revision: 1.4 $ $Date: 2007-04-03 07:55:39 $
 * @author Petr Sígl
 */
public class HibernateActivityDao extends HibernateDaoSupport implements ActivityDao {

	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#get(java.lang.Long)
	 */
	public Activity get(Long pk) {
		// check if activity id is defined
		Assert.notNull(pk);

		Activity activity = (Activity) getHibernateTemplate().get(Activity.class, pk);
		return activity;
	}
	
	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#insert(cz.softinel.retra.activity.Activity)
	 */	
	public Activity insert(Activity activity) {
		getHibernateTemplate().save(activity);
		return activity;
	}

	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#update(cz.softinel.retra.activity.Activity)
	 */
	public void update(Activity activity) {
		getHibernateTemplate().update(activity);
	}

	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#delete(cz.softinel.retra.activity.Activity)
	 */
	public void delete(Activity activity) {
		//TODO: is there some better way, to do delete - without load?
		//must be at first loaded
		load(activity);
		//than deleted
		getHibernateTemplate().delete(activity);
	}

	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#findAll()
	 */
	public List<Activity> selectAll() {
		@SuppressWarnings("unchecked")
		List<Activity> result = getHibernateTemplate().loadAll(Activity.class);
		return result;
	}

	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#selectAllNotDeleted()
	 */
	@SuppressWarnings("unchecked")
	public List<Activity> selectAllNotDeleted() {

		Object[] states = new Object[] {Activity.STATE_DELETED}; 

		Session session = getSession();
		Query query = session.getNamedQuery("Activity.selectAllWithoutStates");
		query.setParameterList("states", states);
		
		try {
			return query.list();
		} finally{
			releaseSession(session);
		}
	}
	
	
	/**
	 * @see cz.softinel.retra.activity.dao.ActivityDao#load(cz.softinel.retra.activity.Activity)
	 */
	public void load(Activity activity) {
		// check if activty is defined and has defined pk and than load it
		Assert.notNull(activity);
		Assert.notNull(activity.getPk());
		getHibernateTemplate().load(activity, activity.getPk());
	}
}