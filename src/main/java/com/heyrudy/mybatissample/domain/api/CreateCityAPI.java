package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.AppScopedLocator;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.CityDbKey;
import cyclops.control.Reader;

public class CreateCityAPI {

    public static final CreateCityAPI INSTANCE = new CreateCityAPI();

    private CreateCityAPI() {
        super();
    }

    public Reader<AppScopedLocator, FullCity> execute(final FullCity fullCity) {
        return locator ->
            locator.getDBService(CityDbKey.INSTANCE)
                .fold(
                    dbServiceNotFoundByLocatorError -> {
                        throw dbServiceNotFoundByLocatorError.toException();
                    },
                    iCityDbSPI -> iCityDbSPI.save(fullCity)
                );
    }
}
