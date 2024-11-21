package com.rent_a_car.mappers;
import com.rent_a_car.entities.City;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRowMapper implements RowMapper<City> {

    @Override
    public City mapRow(ResultSet rs, int rowNum) throws SQLException {

        City car = new City();
        car.setId(rs.getInt("id"));
        car.setPostalCode(rs.getString("postal_code"));
        car.setName(rs.getString("name"));

        return car;
    }
}
