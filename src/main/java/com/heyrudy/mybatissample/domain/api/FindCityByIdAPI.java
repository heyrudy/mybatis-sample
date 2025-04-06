package com.heyrudy.mybatissample.domain.api;

import static java.lang.String.format;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.spi.AppScopedLocator;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.CityDbKey;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class FindCityByIdAPI {

    public static final FindCityByIdAPI INSTANCE = new FindCityByIdAPI();

    private FindCityByIdAPI() {
        super();
    }

    public Reader<AppScopedLocator, Either<CityNotFoundError, FullCity>> execute(
        final CityCriteriaDTO cityCriteriaDTO) {
        return locator ->
            locator.getDBService(CityDbKey.INSTANCE)
                .fold(
                    dbServiceNotFoundByLocatorError -> {
                        throw dbServiceNotFoundByLocatorError.toException();
                    },
                    iCityDbSPI ->
                        Option.ofOptional(
                                iCityDbSPI.findCityById(cityCriteriaDTO.cityId()))
                            .toEither(() -> new CityNotFoundError(
                                format("City with id %d was not found", cityCriteriaDTO.cityId())))
                );
    }
}
