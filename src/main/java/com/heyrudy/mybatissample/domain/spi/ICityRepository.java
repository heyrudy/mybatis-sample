package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.gateway.config.AppScopedConfigLocator;
import java.util.List;
import java.util.Optional;

public interface ICityRepository {

    Workflow<AppScopedConfigLocator, CityNotSavedByRepositoryError, ICity> save(ICity iCity);

    Workflow<AppScopedConfigLocator, CriticalDSLContextNotFoundByConfigLocatorError, List<ICity>> findAll();

    Workflow<AppScopedConfigLocator, CityNotFoundByRepositoryError, Optional<ICity>> findById(
        long id);
}