package com.rent_a_car.entities;

import java.util.Date;

public class Offer {
    private int id;
    private boolean isActive;
    private Date dateCreated;
    private Date dateAccepted;
    private Date requestedFrom;
    private Date requestedTo;
    private boolean rejected;
    private double calculatedPrice;
    private Customer customer;
    private Employee employee;
    private Car car;
    private City city;
}
