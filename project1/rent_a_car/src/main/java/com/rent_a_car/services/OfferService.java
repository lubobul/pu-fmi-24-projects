package com.rent_a_car.services;

import com.rent_a_car.dtos.OfferCreateDTO;
import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.entities.*;
import com.rent_a_car.repositories.*;
import com.rent_a_car.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        offer.setDateCreated(new Date());

        var expenseItems = this.calculateExpenseItems(offer, car);

        var calculatedPrice = expenseItems
                .stream().mapToDouble(ExpenseItem::getPrice).sum();

        offer.setCalculatedPrice(calculatedPrice);

        var createdOffer = this.offerRepository.createOffer(offer);

        var offerDto = new OfferDTO(createdOffer);

        offerDto.setCar(car);
        offerDto.setCity(city);
        offerDto.setCustomer(createdCustomer);
        offerDto.setEmployee(employee);

        //TODO Create ExpenseItems records in DB

        return offerDto;
    }

    public Offer getOffer(int id) {
        return this.offerRepository.getOfferById(id);
    }

    private List<ExpenseItem> calculateExpenseItems(OfferCreateDTO offer, Car car){
        var expenseList = new ArrayList<ExpenseItem>();

        long totalDaysRequested = DateUtils.getDaysBetween(offer.getRequestedFrom(), offer.getRequestedTo());
        long weekendDays = DateUtils.getWeekendDaysBetween(offer.getRequestedFrom(), offer.getRequestedTo());
        long basicDays = totalDaysRequested - weekendDays;
        double costBasicDays = basicDays * car.getPricePerDay();

        var basicCostExpenseItem = new ExpenseItem();
        basicCostExpenseItem.setPrice(costBasicDays);
        basicCostExpenseItem.setType(ExpenseTypes.BASIC_DAYS_EXPENSE);
        expenseList.add(basicCostExpenseItem);

        if(weekendDays > 0){
            // като имайте предвид че в рамките на уикенда цената се увеличава с 10% та.
            double costWeekendDays = weekendDays * car.getPricePerDay() * 1.1;
            var weekendCostExpenseItem = new ExpenseItem();
            weekendCostExpenseItem.setPrice(costWeekendDays);
            weekendCostExpenseItem.setType(ExpenseTypes.WEEKEND_DAYS_EXPENSE);
            expenseList.add(weekendCostExpenseItem);
        }

        //Ако клиента има установени произшествия в миналото плаща допълнителна сума от 200 лв след калкулиране на цената.
        if(offer.getCustomer().getHasPastAccidents()){
            var highRiskCusomerExpenseItem = new ExpenseItem();
            highRiskCusomerExpenseItem.setPrice(200);
            highRiskCusomerExpenseItem.setType(ExpenseTypes.HIGH_RISK_CUSTOMER_FEE);
            expenseList.add(highRiskCusomerExpenseItem);
        }

        return expenseList;
    }
}
