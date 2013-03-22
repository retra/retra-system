/*
 * Created on 9.1.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cz.softinel.retra.employee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import cz.softinel.retra.employee.Employee;
import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.user.User;

public class JdbcEmployeeDao extends SimpleJdbcDaoSupport implements EmployeeDao {

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#get(java.lang.Long)
	 */
	public Employee get(Long pk) {
		StringBuilder sql = new StringBuilder("SELECT u.sis10pk, c.sis12firstName, c.sis12lastName  FROM sis10user u, sis11login l, sis12contactinfo c where u.sis10pk = l.sis10pk and u.sis12pk=c.sis12pk and u.sis10pk = ?");
		Employee result = (Employee) getJdbcTemplate().queryForObject(sql.toString(), new Object[] {pk}, new RowMapper() {
			public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
				Employee employee = new Employee();
				employee.setPk(rs.getLong("sis10pk"));
				User user = new User();
				user.setPk(rs.getLong("sis10pk"));
				employee.setUser(user);
				ContactInfo contactInfo = new ContactInfo();
				contactInfo.setFirstName(rs.getString("sis12firstName"));
				contactInfo.setLastName(rs.getString("sis12lastName"));
				user.setContactInfo(contactInfo);
				return employee;
			}
		});
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

	/**
	 * @see cz.softinel.openproject.mira.dao.EmployeeDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<Employee> findAll() {
		StringBuilder sql = new StringBuilder("SELECT u.sis10pk, c.sis12firstName, c.sis12lastName  FROM sis10user u, sis11login l, sis12contactinfo c where u.sis10pk = l.sis10pk and u.sis12pk=c.sis12pk");
		List<Employee> result = getJdbcTemplate().query(sql.toString(), new RowMapper() {
			public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
				Employee employee = new Employee();
				employee.setPk(rs.getLong("sis10pk"));
				User user = new User();
				user.setPk(rs.getLong("sis10pk"));
				employee.setUser(user);
				ContactInfo contactInfo = new ContactInfo();
				contactInfo.setFirstName(rs.getString("sis12firstName"));
				contactInfo.setLastName(rs.getString("sis12lastName"));
				user.setContactInfo(contactInfo);
				return employee;
			}
		});
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
}
