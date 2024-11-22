package com.rent_a_car.repositories;

import com.rent_a_car.entities.ExpenseItem;
import com.rent_a_car.entities.User;
import com.rent_a_car.mappers.ExpenseItemRowMapper;
import com.rent_a_car.mappers.UserRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

@Repository
public class ExpenseItemRepository {

    private JdbcTemplate db;

    public ExpenseItemRepository(JdbcTemplate db) {
        this.db = db;
    }

    public List<ExpenseItem> createExpenseItems(List<ExpenseItem> expenseItems) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO td_expense_items (is_active, offer_id, type, price, days) ");
        sql.append("VALUES (?, ?, ?, ?, ?)");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = connection -> connection.prepareStatement(String.valueOf(sql), Statement.RETURN_GENERATED_KEYS);
        db.batchUpdate(psc, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ExpenseItem expenseItem = expenseItems.get(i);
                ps.setBoolean(1, true);
                ps.setInt(2, expenseItem.getOfferId());
                ps.setString(3, expenseItem.getType());
                ps.setDouble(4, expenseItem.getPrice());
                ps.setInt(5, expenseItem.getDays());
            }

            @Override
            public int getBatchSize() {
                return expenseItems.size();
            }
        }, keyHolder);

        var keyList = keyHolder
                .getKeyList()
                .stream()
                .map(key -> Integer.parseInt(key.get("ID").toString()))
                .toList();

        return this.getExpenseItemsByIds(keyList);
    }


    public List<ExpenseItem> getExpenseItemsByIds(List<Integer> ids){
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_expense_items WHERE is_active = TRUE");
        query.append(String.format(" AND id IN (%s)", inSql));

        List<ExpenseItem> collection = this.db.query(query.toString(), new ExpenseItemRowMapper(), ids.toArray());

        if (collection.isEmpty()) {
            return null;
        }

        return collection;
    }

    public ExpenseItem getExpenseItemById(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_expense_items WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<ExpenseItem> collection = this.db.query(query.toString(), new Object[]{id}, new ExpenseItemRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }

    public List<ExpenseItem> getExpenseItemByOfferId(int offerId) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_expense_items WHERE is_active = TRUE");
        query.append(" AND offer_id = ?");
        List<ExpenseItem> collection = this.db.query(query.toString(), new Object[]{offerId}, new ExpenseItemRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection;
    }


}
