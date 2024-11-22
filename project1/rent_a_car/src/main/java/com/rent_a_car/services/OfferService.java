package com.rent_a_car.services;

import com.rent_a_car.dtos.CarDTO;
import com.rent_a_car.dtos.CustomerDTO;
import com.rent_a_car.dtos.OfferCreateDTO;
import com.rent_a_car.dtos.OfferDTO;
import com.rent_a_car.entities.*;
import com.rent_a_car.http.PagedResponse;
import com.rent_a_car.repositories.*;
import com.rent_a_car.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OfferService {
    private OfferRepository offerRepository;
    private EmployeeRepository employeeRepository;
    private CarRepository carRepository;
    private CityRepository cityRepository;
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private ExpenseItemRepository expenseItemRepository;

    public OfferService(
            EmployeeRepository employeeRepository,
            OfferRepository offerRepository,
            CarRepository carRepository,
            CityRepository cityRepository,
            UserRepository userRepository,
            CustomerRepository customerRepository,
            ExpenseItemRepository expenseItemRepository
    ) {
        this.offerRepository = offerRepository;
        this.employeeRepository = employeeRepository;
        this.carRepository = carRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.expenseItemRepository = expenseItemRepository;
    }

    public PagedResponse<OfferDTO> getOffers(int page, int pageSize, Map<String, String> filters) {

        //make sure ID is not passed from outside
        filters.remove("customerId");

        Customer customer;

        if (filters.containsKey("customerEmail") || filters.containsKey("customerPhone")) {

            var customerEmail = filters.getOrDefault("customerEmail", "");
            var customerPhone = filters.getOrDefault("customerPhone", "");
            customer = this.customerRepository.findCustomer(customerEmail, customerPhone);

            if (customer == null) {
                return PagedResponse.getEmpty(page, pageSize);
            }

            //TODO Not ideal, but it works for now, perhaps getOffer
            filters.put("customerId", customer.getId() + "");
        } else {
            customer = null;
        }

        PagedResponse<Offer> offerPagedResponse = this.offerRepository.getOffers(page, pageSize, filters);

        var offerDtoCollection = offerPagedResponse.getValues().stream().map(offer -> {
            var offerDTO = new OfferDTO(offer);
            var customerDto = new CustomerDTO(customer);
            offerDTO.setCustomer(customerDto);
            return offerDTO;
        }).toList();

        var carDtoPagedResponse = new PagedResponse<>(offerDtoCollection);
        carDtoPagedResponse.setPage(offerPagedResponse.getPage());
        carDtoPagedResponse.setTotalItems(offerPagedResponse.getTotalItems());
        carDtoPagedResponse.setPageSize(offerPagedResponse.getPageSize());

        return carDtoPagedResponse;
    }

    public OfferDTO createOffer(OfferCreateDTO offer) {

        //TODO It will be better to throw and handle an exception if any of the provided data is problematic
        // However, for now we simply return null in such conditions
        //Check if provided employee exists
        var employee = this.employeeRepository.getEmployeeById(offer.getEmployeeId());

        if (employee == null) {
            return null;
        }

        //Check if provided car exists
        var car = this.carRepository.getCarById(offer.getCarId());

        if (car == null) {
            return null;
        }

        //Check if provided city exists
        var city = this.cityRepository.getCityById(offer.getCityId());

        if (city == null) {
            return null;
        }

        var customer = this.customerRepository.findCustomer(
                    offer.getCustomer().getUser().getEmail(),
                    offer.getCustomer().getUser().getPhone()
                );

        if (customer == null) {
            //Create User
            var createdUser = this.userRepository.createUser(offer.getCustomer().getUser());

            if (createdUser == null) {
                return null;
            }

            var customerToCreate = offer.getCustomer();
            customerToCreate.setUser(createdUser);

            //Create Customer
            customer = this.customerRepository.createCustomer(customerToCreate);
        }

        var user = this.userRepository.getUserById(customer.getUserId());

        offer.getCustomer().setId(customer.getId());
        offer.setDateCreated(new Date());

        var expenseItems = this.calculateExpenseItems(offer, car);

        var calculatedPrice = expenseItems
                .stream().mapToDouble(ExpenseItem::getPrice).sum();

        offer.setCalculatedPrice(calculatedPrice);

        //Create Offer
        var createdOffer = this.offerRepository.createOffer(offer);

        var offerDto = new OfferDTO(createdOffer);

        offerDto.setCar(car);
        offerDto.setCity(city);
        var customerDto = new CustomerDTO(customer);
        customerDto.setUser(user);
        offerDto.setCustomer(customerDto);
        offerDto.setEmployee(employee);

        for (var expenseItem : expenseItems) {
            expenseItem.setOfferId(createdOffer.getId());
        }

        //TODO Create ExpenseItems records in DB
        var createdExpenseItems = this.expenseItemRepository.createExpenseItems(expenseItems);

        offerDto.setExpenseItems(createdExpenseItems);

        return offerDto;
    }

    public OfferDTO getOffer(int id) {
        var offer = this.offerRepository.getOfferById(id);
        if(offer == null){
            return null;
        }
        var offerDto = new OfferDTO(offer);
        var customer = this.customerRepository.getCustomerById(offer.getCustomerId());

        if(customer == null){
            return offerDto;
        }

        offerDto.setCustomer(new CustomerDTO(customer));

        var user = this.userRepository.getUserById(customer.getUserId());

        if(user == null){
            return offerDto;
        }

        offerDto.getCustomer().setUser(user);

        var car = this.carRepository.getCarById(offer.getCarId());

        if(car == null){
            return offerDto;
        }

        offerDto.setCar(car);

        var employee = this.employeeRepository.getEmployeeById(offer.getEmployeeId());

        if(employee == null){
            return offerDto;
        }

        offerDto.setEmployee(employee);

        var city = this.cityRepository.getCityById(offer.getCityId());

        if(city == null){
            return offerDto;
        }

        offerDto.setCity(city);

        var expenseItems = this.expenseItemRepository.getExpenseItemByOfferId(offer.getId());

        if(expenseItems == null){
            return offerDto;
        }

        offerDto.setExpenseItems(expenseItems);

        return offerDto;
    }

    private List<ExpenseItem> calculateExpenseItems(OfferCreateDTO offer, Car car) {
        var expenseList = new ArrayList<ExpenseItem>();

        long totalDaysRequested = DateUtils.getDaysBetween(offer.getRequestedFrom(), offer.getRequestedTo());
        long weekendDays = DateUtils.getWeekendDaysBetween(offer.getRequestedFrom(), offer.getRequestedTo());
        long basicDays = totalDaysRequested - weekendDays;
        double costBasicDays = basicDays * car.getPricePerDay();

        var basicCostExpenseItem = new ExpenseItem();
        basicCostExpenseItem.setOfferId(offer.getId());
        basicCostExpenseItem.setPrice(costBasicDays);
        basicCostExpenseItem.setType(ExpenseTypes.BASIC_DAYS_EXPENSE);
        basicCostExpenseItem.setDays((int) basicDays);
        expenseList.add(basicCostExpenseItem);

        if (weekendDays > 0) {
            // като имайте предвид че в рамките на уикенда цената се увеличава с 10% та.
            double costWeekendDays = weekendDays * car.getPricePerDay() * 1.1;
            var weekendCostExpenseItem = new ExpenseItem();
            weekendCostExpenseItem.setOfferId(offer.getId());
            weekendCostExpenseItem.setPrice(costWeekendDays);
            weekendCostExpenseItem.setDays((int) weekendDays);
            weekendCostExpenseItem.setType(ExpenseTypes.WEEKEND_DAYS_EXPENSE);
            expenseList.add(weekendCostExpenseItem);
        }

        //Ако клиента има установени произшествия в миналото плаща допълнителна сума от 200 лв след калкулиране на цената.
        if (offer.getCustomer().getHasPastAccidents()) {
            var highRiskCusomerExpenseItem = new ExpenseItem();
            highRiskCusomerExpenseItem.setOfferId(offer.getId());
            highRiskCusomerExpenseItem.setPrice(200);
            highRiskCusomerExpenseItem.setType(ExpenseTypes.HIGH_RISK_CUSTOMER_FEE);
            highRiskCusomerExpenseItem.setDays(0);
            expenseList.add(highRiskCusomerExpenseItem);
        }

        return expenseList;
    }

    public boolean acceptOffer(int offerId){
        return this.offerRepository.acceptOffer(offerId);
    }

    public boolean deleteOffer(int offerId){
        return this.offerRepository.deleteOffer(offerId);
    }
}
