package com.rent_a_car.repositories;

import com.rent_a_car.dtos.OfferCreateDTO;
import com.rent_a_car.entities.Customer;
import com.rent_a_car.entities.Offer;
import com.rent_a_car.entities.User;
import com.rent_a_car.mappers.CustomerRowMapper;
import com.rent_a_car.mappers.UserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepository {

    private JdbcTemplate db;

    public UserRepository(JdbcTemplate db) {
        this.db = db;
    }

    public User createUser(User user) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO td_users (is_active, first_name, last_name, email, phone, personal_id, address) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?)");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, true);
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getPersonalId());
            ps.setString(7, user.getAddress());
            return ps;
        }, keyHolder);

        if (result > 0 && keyHolder.getKey() != null) {
            int newUserId = keyHolder.getKey().intValue();
            return getUserById(newUserId);
        } else {
            return null;
        }
    }

    public User getUserById(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_users WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<User> collection = this.db.query(query.toString(), new Object[]{id}, new UserRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }


}
