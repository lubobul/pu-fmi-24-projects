package com.rent_a_car.controllers;

import com.rent_a_car.dtos.CarDTO;
import com.rent_a_car.entities.Car;
import com.rent_a_car.http.AppResponse;
import com.rent_a_car.services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cars")
public class CarController {

    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    //Листване на всички автомобили базирано на филтри
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

    //Листване на конкретен автомобил по ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCar(@PathVariable int id){
        var car = this.carService.getCar(id);

        if(car == null) {
            return AppResponse
                    .error()
                    .withCode(HttpStatus.NOT_FOUND)
                    .build();
        }

        return AppResponse
                .success()
                .withData(car)
                .build();
    }

    //Добавяне на нов автомобил
    @PostMapping
    public ResponseEntity<?> createCar(@RequestBody CarDTO car) {
        boolean isCreateSuccessful =  this.carService.createCar(car);

        if(!isCreateSuccessful) {
            return AppResponse.error()
                    .withMessage("Car not created successfully")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Created car successfully")
                .build();
    }

    //Актуализация на съществуващ автомобил
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(@RequestBody Car car) {
        boolean isUpdateSuccessful =  this.carService.updateCar(car);

        if(!isUpdateSuccessful) {
            return AppResponse.error()
                    .withMessage("Car not updated")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Update successful")
                .build();
    }

    //Изтриване на автомобил от системата
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable int id) {
        boolean isDeleteSuccessful =  this.carService.deleteCar(id);

        if(!isDeleteSuccessful) {
            return AppResponse.error()
                    .withMessage("Car not deleted successfully")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Delete successful")
                .build();
    }
}
