package com.rent_a_car.dtos;

import com.rent_a_car.entities.User;

public class CustomerCreateDTO {
    private int id;
    private boolean hasPastAccidents;
    private int cityId;
    private int age;

    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getHasPastAccidents() {
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
