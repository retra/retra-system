package cz.softinel.retra.activity.blo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cz.softinel.retra.activity.Activity;
import cz.softinel.retra.activity.dao.ActivityDao;
import cz.softinel.retra.core.blo.AbstractLogicBean;

/**
 * Implementation of activity logic
 * 
 * @version $Revision: 1.5 $ $Date: 2007-04-03 07:55:39 $
 * @author Radek Pinc, Petr Sígl
 */
public class ActivityLogicImpl extends AbstractLogicBean implements ActivityLogic {

	private ActivityDao activityDao;

	/**
	 * @return the activityDao
	 */
	public ActivityDao getActivityDao() {
		return activityDao;
	}

	/**
	 * @param activityDao the activityDao to set
	 */
	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	/**
	 * @see cz.softinel.retra.activity.blo.ActivityLogic#findAllActivities()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Activity> findAllActivities() {
		return activityDao.selectAll();
	}

	/**
	 * @see cz.softinel.retra.activity.blo.ActivityLogic#findAllNotDeletedActivities()
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Activity> findAllNotDeletedActivities() {
		return activityDao.selectAllNotDeleted();
	}
}
