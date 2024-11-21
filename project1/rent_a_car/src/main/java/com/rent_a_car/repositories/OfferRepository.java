package com.rent_a_car.repositories;

import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.entities.City;
import com.rent_a_car.entities.Offer;
import com.rent_a_car.mappers.CityRowMapper;
import com.rent_a_car.mappers.OfferRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OfferRepository {

    private JdbcTemplate db;

    public OfferRepository(JdbcTemplate db) {
        this.db = db;
    }

    public Offer createOffer(OfferDTO offer) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO td_offers (is_active, date_created, date_accepted, requested_from, requested_to, rejected, calculated_price, customer_id, employee_id, car_id, city_id) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, true);
            ps.setDate(2, new java.sql.Date(offer.getDateCreated().getTime()));
            ps.setDate(3, offer.getDateAccepted() != null ? new java.sql.Date(offer.getDateAccepted().getTime()) : null);
            ps.setDate(4, new java.sql.Date(offer.getRequestedFrom().getTime()));
            ps.setDate(5, new java.sql.Date(offer.getRequestedTo().getTime()));
            ps.setBoolean(6, offer.isRejected());
            ps.setDouble(7, offer.getCalculatedPrice());
            ps.setLong(8, offer.getCustomer().getId());
            ps.setLong(9, offer.getEmployee().getId());
            ps.setLong(10, offer.getCar().getId());
            ps.setLong(11, offer.getCity().getId());
            return ps;
        }, keyHolder);

        if (result > 0 && keyHolder.getKey() != null) {
            int newOfferId = keyHolder.getKey().intValue();
            return getOfferById(newOfferId);
        } else {
            //TODO improve by throw new RuntimeException("Failed to create the offer.");
            return null;
        }
    }

    public Offer getOfferById(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_offers WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<Offer> collection = this.db.query(query.toString(), new Object[]{id}, new OfferRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }

}
