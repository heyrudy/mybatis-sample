package com.heyrudy.mybatissample.domain.model.city;

public final class NullCity implements ICity {

    @Override
    public long getId() {
        return -1;
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
}
