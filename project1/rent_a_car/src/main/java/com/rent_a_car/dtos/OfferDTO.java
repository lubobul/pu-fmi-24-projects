package com.rent_a_car.dtos;

import com.rent_a_car.entities.*;

import java.util.Date;
import java.util.List;

public class OfferDTO {
    private int id;
    private Date dateCreated;
    private Date dateAccepted;
    private Date requestedFrom;
    private Date requestedTo;
    private boolean rejected;
    private double calculatedPrice;
    private CustomerDTO customer;
    private Employee employee;
    private Car car;
    private City city;
    private List<ExpenseItem> expenseItems;

    public OfferDTO(){

    }

    public OfferDTO(Offer offer) {
        this.id = offer.getId();
        this.dateCreated = offer.getDateCreated();
        this.dateAccepted = offer.getDateAccepted();
        this.requestedFrom = offer.getRequestedFrom();
        this.requestedTo = offer.getRequestedTo();
        this.rejected = offer.isRejected();
        this.calculatedPrice = offer.getCalculatedPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateAccepted() {
        return dateAccepted;
    }

    public void setDateAccepted(Date dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    public Date getRequestedFrom() {
        return requestedFrom;
    }

    public void setRequestedFrom(Date requestedFrom) {
        this.requestedFrom = requestedFrom;
    }

    public Date getRequestedTo() {
        return requestedTo;
    }

    public void setRequestedTo(Date requestedTo) {
        this.requestedTo = requestedTo;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public double getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(double calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<ExpenseItem> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<ExpenseItem> expenseItems) {
        this.expenseItems = expenseItems;
    }
}
