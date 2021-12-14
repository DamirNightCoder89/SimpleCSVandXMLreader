package org.example.sample.entity;

import java.util.Objects;

public class Building {

    private String city;
    private String street;
    private int house;
    private int floor;

    public Building() {
    }

    public Building(String city, String street, int house, int floor) {
        this.city = city;
        this.street = street;
        this.house = house;
        this.floor = floor;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouse() {
        return house;
    }

    public int getFloor() {
        return floor;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setHouse(int house)
    {
        this.house = house;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building that = (Building) o;
        return house == that.house
                && floor == that.floor
                && city.trim().equalsIgnoreCase(that.city.trim())
                && street.trim().equalsIgnoreCase(that.street.trim());
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, house, floor);
    }
}
