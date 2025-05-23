package com.heyrudy.mybatissample.domain.model.city;

public final class PartialCityProxy implements ICity {

    private long id;
    private String name;
    private String state;
    private String country;

    public PartialCityProxy() {
        super();
    }

    public static PartialCityProxy builder() {
        return new PartialCityProxy();
    }

    public PartialCityProxy(long id, String name, String country) {
        super();
        this.id = id;
        this.name = name;
        this.country = country;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public PartialCityProxy id(long id) {
        this.id = id;
        return this;
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

    public PartialCityProxy build() {
        return this;
    }
}
