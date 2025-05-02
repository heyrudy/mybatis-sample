package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class CityCriticalDbAdapter implements ICityDbSPI {

    @Override
    public Workflow<AppScopedDependencyLocator, CriticalRepositoryNotFoundByServiceLocatorError, ICity> save(
        ICity iCity) {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalRepositoryNotFoundByServiceLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityRepository ->
                    iCityRepository.save(iCity)
                        .apply(appScopedDependencyLocator)
                        .mapLeft(error ->
                            new CriticalRepositoryNotFoundByServiceLocatorError(
                                error.getMessage()))
                        .map(Function.identity())
                );
    }

    @Override
    public Workflow<AppScopedDependencyLocator, CriticalRepositoryNotFoundByServiceLocatorError, List<ICity>> findCities() {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalRepositoryNotFoundByServiceLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityRepository ->
                    iCityRepository.findAll()
                        .apply(appScopedDependencyLocator)
                        .mapLeft(criticalDSLContextNotFoundByConfigLocatorError ->
                            new CriticalRepositoryNotFoundByServiceLocatorError(
                                criticalDSLContextNotFoundByConfigLocatorError.getMessage()))
                        .map(Function.identity())
                );
    }

    @Override
    public Workflow<AppScopedDependencyLocator, CriticalRepositoryNotFoundByServiceLocatorError, Optional<ICity>> findCityById(
        long id) {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalRepositoryNotFoundByServiceLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityRepository ->
                    iCityRepository.findById(id)
                        .apply(appScopedDependencyLocator)
                        .mapLeft(cityNotFoundByRepositoryError ->
                            new CriticalRepositoryNotFoundByServiceLocatorError(
                                cityNotFoundByRepositoryError.getMessage()))
                        .map(Function.identity())
                );
    }
}
