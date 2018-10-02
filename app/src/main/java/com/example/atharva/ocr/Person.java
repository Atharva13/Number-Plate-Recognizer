package com.example.atharva.ocr;

public class Person {
    public String name,location,car_number;

    public Person(){}

    public Person(String name, String location, String car_number) {
        this.name = name;
        this.location = location;
        this.car_number = car_number;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }
}