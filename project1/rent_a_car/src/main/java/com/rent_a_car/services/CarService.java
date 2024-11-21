package com.rent_a_car.services;
import com.rent_a_car.entities.Car;
import com.rent_a_car.http.PagedResponse;
import com.rent_a_car.repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CarService {
    private CarRepository carRepository;

    public CarService(CarRepository carRepository){
        this.carRepository = carRepository;
    }
    public PagedResponse<Car> getCars(int page, int pageSize, Map<String, String> filters) {

        var cars = this.carRepository.getCars(page, pageSize, filters);

        return cars;
    }
}
