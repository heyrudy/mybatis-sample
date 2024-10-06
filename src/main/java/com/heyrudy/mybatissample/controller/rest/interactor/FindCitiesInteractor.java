package com.heyrudy.mybatissample.controller.rest.interactor;

import com.heyrudy.mybatissample.domain.api.IFindCitiesInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICitiesFoundDbSPI;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public record FindCitiesInteractor(ICitiesFoundDbSPI gateway)
    implements IFindCitiesInteractorAPI {

    public List<FullCity> execute() {
        return gateway.findCities();
    }
}
