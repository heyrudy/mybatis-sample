package com.heyrudy.mybatissample.gateway.rest;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import org.springframework.web.service.annotation.GetExchange;

public interface CityClient {

    @GetExchange
    ICity getCityById(Long id);

}