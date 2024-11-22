package com.rent_a_car.dtos;

import com.rent_a_car.entities.Customer;
import com.rent_a_car.entities.User;

public class CustomerDTO {
    private int id;
    private boolean hasPastAccidents;
    private int cityId;
    private int age;

    private User user;

    public CustomerDTO() {

    }

    public CustomerDTO(Customer customer){
        this.setAge(customer.getAge());
        this.setId(customer.getId());
        this.setCityId(customer.getCityId());
        this.setHasPastAccidents(customer.hasPastAccidents());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasPastAccidents() {
        return hasPastAccidents;
    }

    public void setHasPastAccidents(boolean hasPastAccidents) {
        this.hasPastAccidents = hasPastAccidents;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
