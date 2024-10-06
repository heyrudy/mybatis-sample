package com.heyrudy.mybatissample.controller.rest.interactor;

import static java.lang.String.format;

import com.heyrudy.mybatissample.domain.api.IFindCityByIdInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.spi.ICityFoundByIdDbSPI;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

@Service
public record FindCityByIdInteractor(ICityFoundByIdDbSPI gateway)
    implements IFindCityByIdInteractorAPI {

    public Either<CityNotFoundError, FullCity> execute(final CityCriteriaDTO cityCriteriaDTO) {
        return Option.ofOptional(gateway.findCityById(cityCriteriaDTO.cityId()))
            .toEither(() -> new CityNotFoundError(
                format("City with %d was not found", cityCriteriaDTO.cityId())));
    }
}
