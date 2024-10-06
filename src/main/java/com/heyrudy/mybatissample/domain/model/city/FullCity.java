package com.heyrudy.mybatissample.domain.model.city;

public final class FullCity implements ICity {

    private long id;
    private String name;
    private String state;
    private String country;

    public FullCity() {
    }

    public static FullCity builder() {
        return new FullCity();
    }

    public FullCity(long id, String name, String state, String country) {
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

    public FullCity id(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FullCity name(String name) {
        this.name = name;
        return this;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public FullCity state(String state) {
        this.state = state;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public FullCity country(String country) {
        this.country = country;
        return this;
    }

    public FullCity build() {
        return this;
    }
}
