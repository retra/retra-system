/*
 * Created on 9.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.util.Assert;

import cz.softinel.retra.employee.Employee;
import cz.softinel.uaf.orm.hibernate.AbstractHibernateDao;

/**
 * @author Radek Pinc
 *
 */
public class HibernateEmployeeDao extends AbstractHibernateDao implements EmployeeDao {

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#get(java.lang.Long)
	 */
	public Employee get(Long pk) {
		// check if employee id is defined
		Assert.notNull(pk);

		Employee employee = (Employee) getHibernateTemplate().get(Employee.class, pk);
		// TODO: Find some consistant solution for automatic loading
		return employee;
	}

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#insert(cz.softinel.openproject.mira.bean.Employee)
	 */
	public Employee insert(Employee employee) {
		getHibernateTemplate().save(employee);
		return employee;
	}

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#update(cz.softinel.openproject.mira.bean.Employee)
	 */
	public void update(Employee employee) {
		getHibernateTemplate().update(employee);
	}

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#delete(cz.softinel.openproject.mira.bean.Employee)
	 */
	public void delete(Employee employee) {
		// TODO: is there some better way, to do delete - without load?
		// must be at first loaded
		load(employee);
		// than deleted
		getHibernateTemplate().delete(employee);
	}

	/** @see cz.softinel.retra.employee.dao.EmployeeDao#findAll(boolean, boolean) */
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> findAll(final boolean onlyActive, final boolean onlyWorkLogging) {
		final Session session = getSession();
		try {
			final Query query = session.getNamedQuery("Employee.findAllFetch");
			query.setParameter("onlyActive", onlyActive);
			query.setParameter("onlyWorkLogging", onlyWorkLogging);
			return query.list();
		} finally {
			releaseSession(session);
		}
	}

	public List<Employee> findAllForGenerate(final Date startDate, final Date finishDate) {
		throw new IllegalStateException("Not supported...");
	}

	public List<Employee> findAllActiveEmployeesWithWorklogIdInInterval(final Date startDate, final Date finishDate) {
		throw new IllegalStateException("Not supported...");
	}
	
	/**
	 * @see cz.softinel.retra.employee.dao.EmployeeDao#load(cz.softinel.retra.employee.Employee)
	 */
	public void load(Employee employee) {
		// check if employee is defined and has defined pk and than load it
		Assert.notNull(employee);
		Assert.notNull(employee.getPk());
		getHibernateTemplate().load(employee, employee.getPk());
	}

	public void loadAndLoadLazy(Employee employee) {
		Session session = getSession();
		session.load(employee, employee.getPk());
		if (employee.getProjects() != null) {
			employee.getProjects().size();
		}
		releaseSession(session);
	}
}
