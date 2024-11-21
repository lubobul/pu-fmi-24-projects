package com.rent_a_car.services;

import com.rent_a_car.dtos.CarDTO;
import com.rent_a_car.entities.Car;
import com.rent_a_car.http.PagedResponse;
import com.rent_a_car.repositories.CarRepository;
import com.rent_a_car.repositories.CityRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarService {
    private CarRepository carRepository;
    private CityRepository cityRepository;

    public CarService(
            CarRepository carRepository,
            CityRepository cityRepository
    ) {
        this.carRepository = carRepository;
        this.cityRepository = cityRepository;
    }

    public PagedResponse<CarDTO> getCars(int page, int pageSize, Map<String, String> filters) {
        //make sure ID is not passed from outside
        filters.remove("cityId");

        if(filters.containsKey("location")){
            var city = this.cityRepository.findCityByName(filters.get("location"));
            if(city == null){
                return PagedResponse.getEmpty(page, pageSize);
            }

            //TODO Not ideal, but it works for now, perhaps getCars
            filters.put("cityId", city.getId() + "");
        }

        var carPagedResponse = this.carRepository.getCars(page, pageSize, filters);

        Set<Integer> uniqueCityIds = carPagedResponse
                .getValues()
                .stream()
                .map(Car::getCityId)
                .collect(Collectors.toSet());

        var citiesMap = this.cityRepository.findCitiesByIds(uniqueCityIds.stream().toList());

        var carDtoCollection = carPagedResponse.getValues().stream().map(car -> {
            var carDto = new CarDTO(car);
            carDto.setCity(citiesMap.get(car.getCityId()));
            return carDto;
        }).toList();

        var carDtoPagedResponse = new PagedResponse<>(carDtoCollection);
        carDtoPagedResponse.setPage(carPagedResponse.getPage());
        carDtoPagedResponse.setTotalItems(carPagedResponse.getTotalItems());
        carDtoPagedResponse.setPageSize(carPagedResponse.getPageSize());

        return carDtoPagedResponse;
    }

    public CarDTO getCar(int id) {
        var car = this.carRepository.getCarById(id);

        if (car == null) {
            return null;
        }

        var carDto = new CarDTO(car);

        var city = this.cityRepository.getCityById(car.getCityId());
        carDto.setCity(city);

        return carDto;
    }

    public boolean createCar(CarDTO car) {

        var city = this.cityRepository.findCity(car.getCity());

        //Внимание. Всички автомобили са разпределени само и единствено между 4 - те града
        // като системата не трябва да допуска вариант при който се добя или актуализира автомобил извън предварително зададените градове.
        if (city == null) {
            //TODO Of course here it will e better to throw a specific exception, but we are Ok like that for now.
            return false;
        }

        car.setCity(city);
        return this.carRepository.createCar(car);
    }

    public boolean updateCar(CarDTO car) {
        var city = this.cityRepository.findCity(car.getCity());

        //Внимание. Всички автомобили са разпределени само и единствено между 4 - те града
        // като системата не трябва да допуска вариант при който се добя или актуализира автомобил извън предварително зададените градове.
        if (city == null) {
            //TODO Of course here it will e better to throw a specific exception, but we are Ok like that for now.
            return false;
        }
        car.setCity(city);
        return this.carRepository.updateCar(car);
    }

    public boolean deleteCar(int id) {
        return this.carRepository.deleteCar(id);
    }
}
