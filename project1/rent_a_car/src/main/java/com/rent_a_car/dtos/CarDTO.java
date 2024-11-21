package com.rent_a_car.dtos;

import com.rent_a_car.entities.Car;
import com.rent_a_car.entities.City;

public class CarDTO {
    private int id;
    private int modelYear;
    private String model;
    private String brand;
    private int kilometersDriven;
    private double pricePerDay;
    private City city;

    public CarDTO(Car car){
        this.id = car.getId();
        this.modelYear = car.getModelYear();
        this.model = car.getModel();
        this.brand = car.getBrand();
        this.kilometersDriven = car.getKilometersDriven();
        this.pricePerDay = car.getPricePerDay();
    }

    public CarDTO(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getKilometersDriven() {
        return kilometersDriven;
    }

    public void setKilometersDriven(int kilometersDriven) {
        this.kilometersDriven = kilometersDriven;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
