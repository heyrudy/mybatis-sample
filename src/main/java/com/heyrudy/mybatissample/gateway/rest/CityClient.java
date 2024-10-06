package com.heyrudy.mybatissample.gateway.rest;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import org.springframework.web.service.annotation.GetExchange;

public interface CityClient {

    @GetExchange
    FullCity getCityById(Long id);

}
