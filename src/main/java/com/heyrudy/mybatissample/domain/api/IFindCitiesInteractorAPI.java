package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import java.util.List;

public interface IFindCitiesInteractorAPI {

    /**
     * @return All cities fetch from the database
     */
    List<FullCity> execute();
}
