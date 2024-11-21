package com.rent_a_car.entities;

public class Customer extends User{
    private int id;
    private boolean isActive;
    private boolean hasPastAccidents;
    private City city;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isHasPastAccidents() {
        return hasPastAccidents;
    }

    public void setHasPastAccidents(boolean hasPastAccidents) {
        this.hasPastAccidents = hasPastAccidents;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
