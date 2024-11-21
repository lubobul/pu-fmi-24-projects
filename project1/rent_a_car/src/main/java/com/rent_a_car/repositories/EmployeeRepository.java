package com.rent_a_car.repositories;
import com.rent_a_car.entities.Employee;
import com.rent_a_car.mappers.EmployeeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class EmployeeRepository {

    private JdbcTemplate db;

    public EmployeeRepository(JdbcTemplate db) {
        this.db = db;
    }


    public Employee getEmployeeById(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM td_employees WHERE is_active = TRUE");
        query.append(" AND id = ?");
        List<Employee> collection = this.db.query(query.toString(), new Object[]{id}, new EmployeeRowMapper());

        if (collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }


}
