package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalAppScopedConfigLocatorKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class CityCriticalDbAdapter implements ICityDbSPI {

    @Override
    public Workflow<AppScopedServiceLocator, CriticalRepositoryNotFoundByServiceLocatorError, ICity> save(
        ICity iCity) {
        return appScopedServiceLocator ->
            appScopedServiceLocator.getCriticalConfig(CriticalAppScopedConfigLocatorKey.INSTANCE)
                .bimap(
                    missingCriticalConfigError ->
                        new CriticalRepositoryNotFoundByServiceLocatorError(
                            missingCriticalConfigError.getMessage()),
                    appScopedConfigLocator ->
                        appScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE)
                            .bimap(
                                Function.identity(),
                                cityRepository ->
                                    cityRepository.save(iCity).apply(appScopedConfigLocator)
                                        .bimap(
                                            criticalDSLContextNotFoundByConfigLocatorError ->
                                                new CriticalRepositoryNotFoundByServiceLocatorError(
                                                    criticalDSLContextNotFoundByConfigLocatorError.getMessage()),
                                            Function.identity()
                                        )
                            ).flatMap(Function.identity())
                ).flatMap(Function.identity());
    }

    @Override
    public Workflow<AppScopedServiceLocator, CriticalRepositoryNotFoundByServiceLocatorError, List<ICity>> findCities() {
        return appScopedServiceLocator ->
            appScopedServiceLocator.getCriticalConfig(CriticalAppScopedConfigLocatorKey.INSTANCE)
                .bimap(
                    missingCriticalConfigError ->
                        new CriticalRepositoryNotFoundByServiceLocatorError(
                            missingCriticalConfigError.getMessage()),
                    appScopedConfigLocator ->
                        appScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE)
                            .bimap(
                                Function.identity(),
                                cityRepository ->
                                    cityRepository.findAll().apply(appScopedConfigLocator)
                                        .bimap(
                                            criticalDSLContextNotFoundByConfigLocatorError ->
                                                new CriticalRepositoryNotFoundByServiceLocatorError(
                                                    criticalDSLContextNotFoundByConfigLocatorError.getMessage()),
                                            Function.identity()
                                        )
                            ).flatMap(Function.identity())
                ).flatMap(Function.identity());
    }

    @Override
    public Workflow<AppScopedServiceLocator, CriticalRepositoryNotFoundByServiceLocatorError, Optional<ICity>> findCityById(
        long id) {
        return appScopedServiceLocator ->
            appScopedServiceLocator.getCriticalConfig(CriticalAppScopedConfigLocatorKey.INSTANCE)
                .bimap(
                    missingCriticalConfigError ->
                        new CriticalRepositoryNotFoundByServiceLocatorError(
                            missingCriticalConfigError.getMessage()),
                    appScopedConfigLocator ->
                        appScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE)
                            .bimap(
                                Function.identity(),
                                cityRepository ->
                                    cityRepository.findById(id).apply(appScopedConfigLocator)
                                        .bimap(
                                            criticalDSLContextNotFoundByConfigLocatorError ->
                                                new CriticalRepositoryNotFoundByServiceLocatorError(
                                                    criticalDSLContextNotFoundByConfigLocatorError.getMessage()),
                                            Function.identity()
                                        )
                            ).flatMap(Function.identity())
                ).flatMap(Function.identity());
    }
}
