package com.rent_a_car.mappers;

import com.rent_a_car.entities.Customer;
import com.rent_a_car.entities.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setAge(rs.getInt("age"));
        customer.setCityId(rs.getInt("city_id"));
        customer.setUserId(rs.getInt("user_id"));
        customer.setHasPastAccidents(rs.getBoolean("has_past_accidents"));

        return customer;
    }
}
