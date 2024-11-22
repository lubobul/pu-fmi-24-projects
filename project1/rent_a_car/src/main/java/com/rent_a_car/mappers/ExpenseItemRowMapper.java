package com.rent_a_car.mappers;

import com.rent_a_car.entities.ExpenseItem;
import com.rent_a_car.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseItemRowMapper implements RowMapper<ExpenseItem> {

    @Override
    public ExpenseItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        ExpenseItem expenseItem = new ExpenseItem();
        expenseItem.setId(rs.getInt("id"));
        expenseItem.setDays(rs.getInt("days"));
        expenseItem.setOfferId(rs.getInt("offer_id"));
        expenseItem.setPrice(rs.getDouble("price"));
        expenseItem.setType(rs.getString("type"));

        return expenseItem;
    }
}
