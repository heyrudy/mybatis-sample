package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import java.util.List;

public interface ICitiesFoundDbSPI {

    List<FullCity> findCities();
}
