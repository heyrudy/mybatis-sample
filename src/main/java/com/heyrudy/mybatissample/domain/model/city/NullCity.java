package com.heyrudy.mybatissample.domain.model.city;

public final class NullCity implements ICity {

    private Long id;

    public NullCity() {
        super();
    }

    public static NullCity builder() {
        return new NullCity();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return "No name";
    }

    @Override
    public String getState() {
        return "No state";
    }

    @Override
    public String getCountry() {
        return "No country";
    }

    public NullCity build() {
        return this;
    }
}