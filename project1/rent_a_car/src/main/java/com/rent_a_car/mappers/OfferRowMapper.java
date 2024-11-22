package com.rent_a_car.mappers;
import com.rent_a_car.entities.City;
import com.rent_a_car.entities.Offer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfferRowMapper implements RowMapper<Offer> {

    @Override
    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {

        Offer offer = new Offer();
        offer.setId(rs.getInt("id"));
        offer.setRejected(rs.getBoolean("rejected"));
        offer.setRequestedFrom(rs.getTimestamp("requested_from"));
        offer.setRequestedTo(rs.getTimestamp("requested_to"));
        offer.setDateAccepted(rs.getTimestamp("date_accepted"));
        offer.setDateCreated(rs.getTimestamp("date_created"));
        offer.setCalculatedPrice(rs.getDouble("calculated_price"));
        offer.setCarId(rs.getInt("car_id"));
        offer.setCityId(rs.getInt("city_id"));
        offer.setCustomerId(rs.getInt("customer_id"));
        offer.setEmployeeId(rs.getInt("employee_id"));

        return offer;
    }
}
