package com.rent_a_car.mappers;
import com.rent_a_car.entities.Employee;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {

        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setPosition(rs.getString("position"));
        employee.setUserId(rs.getInt("user_id"));

        return employee;
    }
}
