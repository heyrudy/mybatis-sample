package com.heyrudy.mybatissample.domain.model.city;

public class City {

    private long id;
    private String name;
    private String state;
    private String country;

    public City() {
    }

    public static City builder() {
        return new City();
    }

    public City(long id, String name, String state, String country) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public City id(long id) {
        this.id = id;
        return this;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City name(String name) {
        this.name = name;
        return this;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public City state(String state) {
        this.state = state;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public City country(String country) {
        this.country = country;
        return this;
    }

    public City build() {
        return this;
    }
}
