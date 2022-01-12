package cz.softinel.retra.employee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cz.softinel.retra.employee.Employee;
import cz.softinel.retra.icompany.Icompany;
import cz.softinel.sis.contactinfo.ContactInfo;
import cz.softinel.sis.user.User;

public class EmployeeRowMapper implements RowMapper {

	@Override
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
		Icompany icompany = new Icompany();
		icompany.setCode(rs.getString("mir18code"));
		icompany.setName(rs.getString("mir18name"));
		employee.setIcompany(icompany);
		return employee;
	}

}
