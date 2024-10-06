package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import java.util.Optional;

public interface ICityFoundByIdDbSPI {

    Optional<FullCity> findCityById(long id);
}
