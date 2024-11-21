package com.rent_a_car.repositories;

import com.rent_a_car.dtos.CustomerCreateDTO;
import com.rent_a_car.entities.Customer;
import com.rent_a_car.mappers.CustomerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CustomerRepository {

    private JdbcTemplate db;

    public CustomerRepository(JdbcTemplate db) {
        this.db = db;
    }


    public Customer createCustomer(CustomerCreateDTO customer) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO td_customers (is_active, has_past_accidents, age, city_id, user_id) ");
        sql.append("VALUES (?, ?, ?, ?, ?)");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, true);
            ps.setBoolean(2, customer.getHasPastAccidents());
            ps.setInt(3, customer.getAge());
            ps.setInt(4, customer.getCityId());
            ps.setInt(5, customer.getUser().getId());
            return ps;
        }, keyHolder);

        if (result > 0 && keyHolder.getKey() != null) {
            int newCustomerId = keyHolder.getKey().intValue();
            return getCustomerById(newCustomerId);
        } else {
            return null;
        }
    }


    public Customer getCustomerById(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_customers WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<Customer> collection = this.db.query(query.toString(), new Object[]{id}, new CustomerRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }
}
