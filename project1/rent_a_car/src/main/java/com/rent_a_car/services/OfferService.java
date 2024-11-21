package com.rent_a_car.services;

import com.rent_a_car.dtos.OfferCreateDTO;
import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.entities.Customer;
import com.rent_a_car.entities.Offer;
import com.rent_a_car.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class OfferService {
    private OfferRepository offerRepository;
    private EmployeeRepository employeeRepository;
    private CarRepository carRepository;
    private CityRepository cityRepository;
    private UserRepository userRepository;
    private CustomerRepository customerRepository;

    public OfferService(
            EmployeeRepository employeeRepository,
            OfferRepository offerRepository,
            CarRepository carRepository,
            CityRepository cityRepository,
            UserRepository userRepository,
            CustomerRepository customerRepository
    ) {
        this.offerRepository = offerRepository;
        this.employeeRepository = employeeRepository;
        this.carRepository = carRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public OfferDTO createOffer(OfferCreateDTO offer) {

        var employee = this.employeeRepository.getEmployeeById(offer.getEmployeeId());

        if(employee == null){
            return null;
        }

        var car = this.carRepository.getCarById(offer.getCarId());

        if(car == null){
            return null;
        }

        var city = this.cityRepository.getCityById(offer.getCityId());

        if(city == null){
            return null;
        }

        var createdUser = this.userRepository.createUser(offer.getCustomer().getUser());

        if(createdUser == null){
            return null;
        }

        var customerToCreate = offer.getCustomer();
        customerToCreate.setUser(createdUser);

        var createdCustomer = this.customerRepository.createCustomer(customerToCreate);

        offer.getCustomer().setId(createdCustomer.getId());
        var createdOffer = this.offerRepository.createOffer(offer);

        var offerDto = new OfferDTO(createdOffer);

        offerDto.setCar(car);
        offerDto.setCity(city);
        offerDto.setCustomer(createdCustomer);
        offerDto.setEmployee(employee);

        return offerDto;
    }

    public Offer getOffer(int id) {
        return this.offerRepository.getOfferById(id);
    }
}
