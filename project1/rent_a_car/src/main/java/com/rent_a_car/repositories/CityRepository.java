package com.rent_a_car.repositories;

import com.rent_a_car.entities.Car;
import com.rent_a_car.entities.City;
import com.rent_a_car.http.PagedResponse;
import com.rent_a_car.mappers.CarRowMapper;
import com.rent_a_car.mappers.CityRowMapper;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CityRepository {

    private JdbcTemplate db;

    public CityRepository(JdbcTemplate db) {
        this.db = db;
    }

    public City getCity(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_cities WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<City> collection = this.db.query(query.toString(), new Object[]{id}, new CityRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);

    }

    public City findCity(City city) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_cities WHERE is_active = TRUE");
        query.append(" AND name LIKE ?");
        query.append(" OR postal_code LIKE ?");

        List<Object> params = new ArrayList<>();
        params.add("%" + city.getName() + "%");
        params.add("%" + city.getPostalCode() + "%");

        List<City> collection = this.db.query(query.toString(), params.toArray(), new CityRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);

    }

    public Map<Integer, City> findCitiesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(", "));
        String sql = "SELECT * FROM td_cities WHERE id IN (" + placeholders + ")";
        List<City> cities = db.query(sql, ids.toArray(), new CityRowMapper());

        return cities.stream().collect(Collectors.toMap(City::getId, city -> city));
    }

}
