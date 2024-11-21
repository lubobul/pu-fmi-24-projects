package com.rent_a_car.controllers;

import com.rent_a_car.http.AppResponse;
import com.rent_a_car.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/cars")
public class CarController {

    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<?> getCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam Map<String, String> filters
    ) {
        var carsPagedResponse = this.carService.getCars(page, pageSize, filters);
        return AppResponse
                .success()
                .withData(carsPagedResponse)
                .build();
    }
}
