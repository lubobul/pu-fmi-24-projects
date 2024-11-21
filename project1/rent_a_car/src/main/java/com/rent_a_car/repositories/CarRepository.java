package com.rent_a_car.repositories;

import com.rent_a_car.entities.Car;
import com.rent_a_car.http.PagedResponse;
import com.rent_a_car.mappers.CarRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CarRepository {

    private JdbcTemplate db;

    public CarRepository(JdbcTemplate db) {
        this.db = db;
    }

    public PagedResponse<Car> getCars(int page, int pageSize, Map<String, String> filters) {
        StringBuilder sql = new StringBuilder("SELECT * FROM td_cars WHERE is_active = TRUE");
        List<Object> params = new ArrayList<>();

        filters.forEach((key, value) -> {
            switch (key) {
                case "modelYear":
                    sql.append(" AND model_year = ?");
                    params.add(Integer.parseInt(value));
                    break;
                case "model":
                    sql.append(" AND model LIKE ?");
                    params.add("%" + value + "%");
                    break;
                case "brand":
                    sql.append(" AND brand LIKE ?");
                    params.add("%" + value + "%");
                    break;
                case "kilometersDriven":
                    sql.append(" AND kilometers_driven = ?");
                    params.add(Integer.parseInt(value));
                    break;
                case "pricePerDay":
                    sql.append(" AND price_per_day = ?");
                    params.add(Double.parseDouble(value));
                    break;
                default:
                    break;
            }
        });

        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") AS total";
        var totalItems = this.db.queryForObject(countSql, params.toArray(), Integer.class);

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(page * pageSize);

        var cars = this.db.query(sql.toString(), params.toArray(), new CarRowMapper());

        var pagedResponse = new PagedResponse(cars);
        pagedResponse.setPage(page);
        pagedResponse.setPageSize(pageSize);
        pagedResponse.setTotalItems(totalItems);
        return pagedResponse;
    }
}
