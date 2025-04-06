package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.AppScopedLocator;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.CityDbKey;
import cyclops.control.Reader;
import java.util.List;

public class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    public Reader<AppScopedLocator, List<FullCity>> execute() {
        return locator ->
            locator.getDBService(CityDbKey.INSTANCE)
                .fold(
                    dbServiceNotFoundByLocatorError -> {
                        throw dbServiceNotFoundByLocatorError.toException();
                    },
                    iCityDbSPI -> iCityDbSPI.findCities().stream().toList()
                );
    }
}
