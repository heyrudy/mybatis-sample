package com.heyrudy.mybatissample.domain.model.city;

public final class PartialCityProxy implements ICity {

    private Long id;
    private String name;
    private String state;
    private String country;

    public PartialCityProxy() {
        super();
    }

    public static PartialCityProxy builder() {
        return new PartialCityProxy();
    }

    public PartialCityProxy(Long id, String name, String country) {
        super();
        this.id = id;
        this.name = name;
        this.country = country;
    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public void setId(Long id) {
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