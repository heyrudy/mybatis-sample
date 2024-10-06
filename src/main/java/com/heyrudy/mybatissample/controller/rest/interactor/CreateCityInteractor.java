package com.heyrudy.mybatissample.controller.rest.interactor;

import com.heyrudy.mybatissample.domain.api.ICreateCityInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICityRegisterDbSPI;
import org.springframework.stereotype.Service;

@Service
public record CreateCityInteractor(ICityRegisterDbSPI gateway)
    implements ICreateCityInteractorAPI {

    public FullCity execute(final FullCity fullCity) {
        return gateway.save(fullCity);
    }
}
