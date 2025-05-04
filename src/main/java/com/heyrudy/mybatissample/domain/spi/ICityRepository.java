package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import java.util.List;
import java.util.Optional;

public interface ICityRepository {

    Workflow<AppScopedDependencyLocator, CityNotSavedByRepositoryError, ICity> save(ICity iCity);

    Workflow<AppScopedDependencyLocator, CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>> findAll();

    Workflow<AppScopedDependencyLocator, CityNotFoundByRepositoryError, Optional<ICity>> findById(
        long id);
}