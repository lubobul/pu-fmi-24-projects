package com.rent_a_car.entities;

public class Customer {
    private int id;
    private boolean hasPastAccidents;
    private int cityId;
    private int age;

    private int userId;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
