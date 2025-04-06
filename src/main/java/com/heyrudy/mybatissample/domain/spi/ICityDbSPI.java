package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import java.util.List;
import java.util.Optional;

public interface ICityDbSPI {

    /**
     * @param fullCity city with all its details to persist in the database
     * @return Persisted city in the database
     */
    FullCity save(FullCity fullCity);

    /**
     * @return All cities fetch from the database
     */
    List<FullCity> findCities();

    /**
     * @param id city's id to fetch from the database
     * @return Required information about a city
     */
    Optional<FullCity> findCityById(long id);
}
