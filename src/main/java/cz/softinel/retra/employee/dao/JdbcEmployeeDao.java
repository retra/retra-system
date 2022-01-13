/*
 * Created on 9.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import cz.softinel.retra.employee.Employee;

public class JdbcEmployeeDao extends SimpleJdbcDaoSupport implements EmployeeDao {

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#get(java.lang.Long)
	 */
	public Employee get(Long pk) {
		StringBuilder sql = new StringBuilder(
				"SELECT u.sis10pk, l.sis11ldaplogin, c.sis12firstName, c.sis12lastName, i.mir18code, i.mir18name "
				+ "FROM sis10user u, sis11login l, sis12contactinfo c, mir04employee AS e LEFT JOIN mir18icompany i ON e.mir18pk = i.mir18pk "
				+ "WHERE u.sis10pk = l.sis10pk and u.sis12pk=c.sis12pk AND e.sis10pk = u.sis10pk and u.sis10pk = ?");
		Employee result = (Employee) getJdbcTemplate().queryForObject(sql.toString(), new Object[] { pk }, new EmployeeRowMapper());
		return result;
	}

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#insert(cz.softinel.openproject.mira.bean.Employee)
	 */
	public Employee insert(Employee employee) {
		throw new IllegalStateException("Not supported...");
	}

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#update(cz.softinel.openproject.mira.bean.Employee)
	 */
	public void update(Employee employee) {
		throw new IllegalStateException("Not supported...");
	}

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#delete(cz.softinel.openproject.mira.bean.Employee)
	 */
	public void delete(Employee employee) {
		throw new IllegalStateException("Not supported...");
	}

	/** @see cz.softinel.retra.employee.dao.EmployeeDao#findAll(boolean, boolean) */
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> findAll(final boolean onlyActive, final boolean onlyWorkLogging) {
		StringBuilder sql = new StringBuilder("SELECT u.sis10pk, l.sis11ldaplogin, c.sis12firstName, c.sis12lastName, i.mir18code, i.mir18name");
		sql.append(" FROM sis10user u, sis11login l, sis12contactinfo c, mir04employee AS e LEFT JOIN mir18icompany i ON e.mir18pk = i.mir18pk");
		sql.append(" WHERE u.sis10pk=l.sis10pk AND u.sis12pk=c.sis12pk AND e.sis10pk = u.sis10pk");
		if (onlyActive) {
			sql.append(" AND u.sis10state = 'A'");
		}
		if (onlyWorkLogging) {
			sql.append(" AND e.mir04worklog = 1");
		}
		List<Employee> result = getJdbcTemplate().query(sql.toString(), new EmployeeRowMapper());
		return result;
	}

	@SuppressWarnings("unchecked")	
	public List<Employee> findAllForGenerate(final Date startDate, final Date finishDate) {
		StringBuilder sql = new StringBuilder("SELECT").append("\n");
		sql.append(" u.sis10pk, l.sis11ldaplogin, c.sis12firstName, c.sis12lastName, i.mir18code, i.mir18name").append("\n");
		sql.append("FROM").append("\n");
		sql.append(" sis11login l,").append("\n");
		sql.append(" sis12contactinfo c,").append("\n");
		sql.append(" sis10user u,").append("\n");
		sql.append(" mir04employee e").append("\n");
		sql.append(" LEFT JOIN mir18icompany i ON e.mir18pk = i.mir18pk").append("\n");
		sql.append("WHERE").append("\n");
		sql.append(" u.sis10pk=l.sis10pk").append("\n");
		sql.append(" AND u.sis12pk=c.sis12pk").append("\n");
		sql.append(" AND e.sis10pk = u.sis10pk").append("\n");
		sql.append(" AND e.mir04igenerate = 1").append("\n");
		sql.append(" AND ").append("\n");
		sql.append(getUserActiveAccordingToHisPlanSubQuery());
		sql.append("ORDER BY").append("\n");
		sql.append(" i.mir18code, c.sis12lastName, c.sis12firstName asc").append("\n");
		Map<String, Object> params = new HashMap<>();
		params.put("fromDate", startDate);
		params.put("toDate", finishDate);
		List<Employee> result = getSimpleJdbcTemplate().getNamedParameterJdbcOperations().query(sql.toString(), params, new EmployeeRowMapper());
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Employee> findAllActiveEmployeesWithWorklogIdInInterval(final Date startDate, final Date finishDate) {
		StringBuilder sql = new StringBuilder("SELECT").append("\n");
		sql.append(" u.sis10pk, l.sis11ldaplogin, c.sis12firstName, c.sis12lastName, i.mir18code, i.mir18name").append("\n");
		sql.append("FROM").append("\n");
		sql.append(" sis11login l,").append("\n");
		sql.append(" sis12contactinfo c,").append("\n");
		sql.append(" sis10user u,").append("\n");
		sql.append(" mir04employee e").append("\n");
		sql.append(" LEFT JOIN mir18icompany i ON e.mir18pk = i.mir18pk").append("\n");
		sql.append("WHERE").append("\n");
		sql.append(" u.sis10pk=l.sis10pk").append("\n");
		sql.append(" AND u.sis12pk=c.sis12pk").append("\n");
		sql.append(" AND e.sis10pk = u.sis10pk").append("\n");
		sql.append(" AND e.mir04worklog = 1").append("\n");
		sql.append(" AND ").append("\n");
		sql.append(getUserActiveAccordingToHisPlanSubQuery());
		sql.append("ORDER BY").append("\n");
		sql.append(" c.sis12lastName, c.sis12firstName asc");
		Map<String, Object> params = new HashMap<>();
		params.put("fromDate", startDate);
		params.put("toDate", finishDate);
		List<Employee> result = getSimpleJdbcTemplate().getNamedParameterJdbcOperations().query(sql.toString(), params, new EmployeeRowMapper());
		logger.debug(sql.toString());
		return result;
	}

	/**
	 * @see cz.softinel.retra.employee.dao.EmployeeDao#load(cz.softinel.retra.employee.Employee)
	 */
	public void load(Employee employee) {
		throw new IllegalStateException("Not supported...");
	}

	public void loadAndLoadLazy(Employee employee) {
		throw new IllegalStateException("Not supported...");
	}
	
	private String getUserActiveAccordingToHisPlanSubQuery() {
        StringBuilder sql = new StringBuilder("(").append("\n");
        sql.append(" (NOT EXISTS").append("\n");
        sql.append("     (SELECT").append("\n");
        sql.append("         p.sis10pk").append("\n");
        sql.append("     FROM").append("\n");
        sql.append("         mir23plan p").append("\n");
        sql.append("     WHERE").append("\n");
        sql.append("         u.sis10pk=p.sis10pk").append("\n");
        sql.append("         AND mir23type='X'").append("\n");
        sql.append("     )").append("\n");
        sql.append(" )").append("\n");
        sql.append(" OR").append("\n");
        sql.append(" (EXISTS").append("\n");
        sql.append("   (SELECT").append("\n");
        sql.append("         p.sis10pk").append("\n");
        sql.append("     FROM").append("\n");
        sql.append("         mir23plan p").append("\n");
        sql.append("     WHERE").append("\n");
        sql.append("         u.sis10pk=p.sis10pk").append("\n");
        sql.append("         AND mir23type='X'").append("\n");
        sql.append("         AND (mir23from IS NULL AND mir23to IS NOT NULL AND (mir23to <= :fromDate OR mir23to >= :toDate))").append("\n");
        sql.append("     )").append("\n");
        sql.append("   AND NOT EXISTS").append("\n");
        sql.append("     (SELECT").append("\n");
        sql.append("         p.sis10pk").append("\n");
        sql.append("     FROM").append("\n");
        sql.append("         mir23plan p").append("\n");
        sql.append("     WHERE").append("\n");
        sql.append("         u.sis10pk=p.sis10pk").append("\n");
        sql.append("         AND mir23type='X'").append("\n");
        sql.append("         AND (mir23to IS NULL AND mir23from IS NOT NULL AND (mir23from <= :fromDate))").append("\n");
        sql.append("     )").append("\n");
        sql.append(" )").append("\n");
        sql.append(" OR").append("\n");
        sql.append(" (EXISTS").append("\n");
        sql.append("     (SELECT").append("\n");
        sql.append("         p.sis10pk").append("\n");
        sql.append("     FROM").append("\n");
        sql.append("         mir23plan p").append("\n");
        sql.append("     WHERE").append("\n");
        sql.append("         u.sis10pk=p.sis10pk").append("\n");
        sql.append("         AND mir23type='X'").append("\n");
        sql.append("         AND (").append("\n");
        sql.append("             (mir23from IS NOT NULL AND mir23to IS NOT NULL AND ((mir23from <= :fromDate AND mir23to <= :fromDate) OR (mir23from >= :toDate AND mir23to >= :toDate)))").append("\n");
        sql.append("             OR (mir23from IS NOT NULL AND mir23to IS NOT NULL AND ((mir23from >= :fromDate AND mir23to >= :fromDate) OR (mir23from <= :toDate AND mir23to <= :toDate)))").append("\n");
        sql.append("         )").append("\n");
        sql.append("     )").append("\n");
        sql.append("   AND NOT EXISTS").append("\n");
        sql.append("     (SELECT").append("\n");
        sql.append("         p.sis10pk").append("\n");
        sql.append("     FROM").append("\n");
        sql.append("         mir23plan p").append("\n");
        sql.append("     WHERE").append("\n");
        sql.append("         u.sis10pk=p.sis10pk").append("\n");
        sql.append("         AND mir23type='X'").append("\n");
        sql.append("         AND (mir23to IS NULL AND mir23from IS NOT NULL AND (mir23from <= :fromDate))").append("\n");
        sql.append("     )").append("\n");
        sql.append(" )").append("\n");
        sql.append(")").append("\n");
        return sql.toString();
	}
}
