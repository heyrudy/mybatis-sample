package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.FullCity;

public interface ICityRegisterDbSPI {

    FullCity save(FullCity fullCity);
}
