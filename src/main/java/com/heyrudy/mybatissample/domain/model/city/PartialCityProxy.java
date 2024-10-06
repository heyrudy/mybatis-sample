package com.heyrudy.mybatissample.domain.model.city;

public final class PartialCityProxy implements ICity {

    private long id;
    private String name;
    private String state;
    private String country;

    public PartialCityProxy(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public String getCountry() {
        return null;
    }
}
