package com.rent_a_car.repositories;

import com.rent_a_car.dtos.CarDTO;
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

    //TODO Perhaps we can put some query validations but for not it's OK
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
                case "cityId":
                    sql.append(" AND city_id = ?");
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

        var cars = this.db.query(sql.toString(), params.toArray(), new CarRowMapper());

        var pagedResponse = new PagedResponse(cars);
        pagedResponse.setPage(page);
        pagedResponse.setPageSize(pageSize);
        pagedResponse.setTotalItems(totalItems);
        return pagedResponse;
    }

    public Car getCarById(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_cars WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<Car> collection = this.db.query(query.toString(), new Object[]{id}, new CarRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);

    }

    public boolean updateCar(CarDTO car) {

        StringBuilder sql = new StringBuilder("UPDATE td_cars SET ");
        sql.append("model_year = ?, ");
        sql.append("model = ?, ");
        sql.append("brand = ?, ");
        sql.append("kilometers_driven = ?, ");
        sql.append("price_per_day = ?, ");
        sql.append("city_id = ? ");
        sql.append("WHERE id = ? AND is_active = TRUE");

        int updated = db.update(
                sql.toString(),
                car.getModelYear(),
                car.getModel(),
                car.getBrand(),
                car.getKilometersDriven(),
                car.getPricePerDay(),
                car.getCity().getId(),
                car.getId());

        if (updated > 1) {
            throw new RuntimeException("More than one car was affected");
        }

        return updated == 1;
    }


    public boolean createCar(CarDTO newCar) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO td_cars (is_active, model_year, model, brand, kilometers_driven, price_per_day, city_id) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?)");

        int result = db.update(sql.toString(),
                true,
                newCar.getModelYear(),
                newCar.getModel(),
                newCar.getBrand(),
                newCar.getKilometersDriven(),
                newCar.getPricePerDay(),
                newCar.getCity().getId());

        if (result > 1) {
            throw new RuntimeException("More than one car was affected");
        }

        return result == 1;
    }

    public boolean deleteCar(int id) {
        // Perform soft delete
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE td_cars ");
        sql.append("SET is_active = FALSE ");
        sql.append("WHERE id = ? AND is_active = TRUE");

        int result = db.update(sql.toString(), id);

        if (result > 1) {
            throw new RuntimeException("More than one car was affected");
        }

        return result == 1;
    }
}
