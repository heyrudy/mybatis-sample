package com.heyrudy.mybatissample.domain.model.city;

public final class FullCity implements ICity {

    private Long id;
    private String name;
    private String state;
    private String country;

    public FullCity() {
        super();
    }

    public static FullCity builder() {
        return new FullCity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FullCity id(Long id) {
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