package com.rent_a_car.repositories;

import com.rent_a_car.dtos.OfferCreateDTO;
import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.entities.Car;
import com.rent_a_car.entities.City;
import com.rent_a_car.entities.Offer;
import com.rent_a_car.http.PagedResponse;
import com.rent_a_car.mappers.CarRowMapper;
import com.rent_a_car.mappers.CityRowMapper;
import com.rent_a_car.mappers.OfferRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class OfferRepository {

    private JdbcTemplate db;

    public OfferRepository(JdbcTemplate db) {
        this.db = db;
    }

    //TODO Perhaps we can put some query validations but for not it's OK
    public PagedResponse<Offer> getOffers(int page, int pageSize, Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("SELECT * FROM td_offers WHERE is_active = TRUE");
        List<Object> params = new ArrayList<>();

        filters.forEach((key, value) -> {
            switch (key) {
                case "requestedTo":
                    sql.append(" AND requested_to LIKE ?");
                    params.add("%" + value + "%");
                    break;
                case "requestedFrom":
                    sql.append(" AND requested_from LIKE ?");
                    params.add("%" + value + "%");
                    break;
                case "dateCreated":
                    sql.append(" AND date_created LIKE ?");
                    params.add("%" + value + "%");
                    break;
                case "dateAccepted":
                    sql.append(" AND date_accepted LIKE ?");
                    params.add("%" + value + "%");
                    break;
                case "customerId":
                    sql.append(" AND customer_id = ?");
                    params.add(Integer.parseInt(value));
                    break;
                default:
                    break;
            }
        });

        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") AS total";
        var totalItems = this.db.queryForObject(countSql, params.toArray(), Integer.class);

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((page - 1) * pageSize);

        var offers = this.db.query(sql.toString(), params.toArray(), new OfferRowMapper());

        var pagedResponse = new PagedResponse(offers);
        pagedResponse.setPage(page);
        pagedResponse.setPageSize(pageSize);
        pagedResponse.setTotalItems(totalItems);
        return pagedResponse;
    }

    public Offer createOffer(OfferCreateDTO offer) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO td_offers (is_active, date_created, date_accepted, requested_from, requested_to, rejected, calculated_price, customer_id, employee_id, car_id, city_id) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, new java.sql.Timestamp(offer.getDateCreated().getTime()));
            ps.setTimestamp(3, offer.getDateAccepted() != null ? new java.sql.Timestamp(offer.getDateAccepted().getTime()) : null);
            ps.setTimestamp(4, new java.sql.Timestamp(offer.getRequestedFrom().getTime()));
            ps.setTimestamp(5, new java.sql.Timestamp(offer.getRequestedTo().getTime()));
            ps.setBoolean(6, offer.isRejected());
            ps.setDouble(7, offer.getCalculatedPrice());
            ps.setLong(8, offer.getCustomer().getId());
            ps.setLong(9, offer.getEmployeeId());
            ps.setLong(10, offer.getCarId());
            ps.setLong(11, offer.getCityId());
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

    public boolean acceptOffer(int offerId){
        // Perform soft delete
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE td_offers ");
        sql.append("SET date_accepted = ? ");
        sql.append("WHERE id = ? AND is_active = TRUE");

        int result = db.update(sql.toString(),
                new java.sql.Timestamp(new Date().getTime()),
                offerId
        );

        if (result > 1) {
            throw new RuntimeException("More than one offer was affected");
        }

        return result == 1;
    }

    public boolean deleteOffer(int id) {
        // Perform soft delete
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE td_offers ");
        sql.append("SET is_active = FALSE ");
        sql.append("WHERE id = ? AND is_active = TRUE");

        int result = db.update(sql.toString(), id);

        if (result > 1) {
            throw new RuntimeException("More than one offer was affected");
        }

        return result == 1;
    }

}
