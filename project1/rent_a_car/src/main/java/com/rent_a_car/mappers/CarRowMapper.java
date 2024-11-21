package com.rent_a_car.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.rent_a_car.entities.Car;
import org.springframework.jdbc.core.RowMapper;

public class CarRowMapper implements RowMapper<Car> {

    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {

        Car car = new Car();
        car.setId(rs.getInt("id"));
        car.setModelYear(rs.getInt("model_year"));
        car.setBrand(rs.getString("brand"));
        car.setModel(rs.getString("model"));
        car.setKilometersDriven(rs.getInt("kilometers_driven"));
        car.setPricePerDay(rs.getDouble("price_per_day"));
        car.setCityId(rs.getInt("city_id"));

        return car;
    }
}
