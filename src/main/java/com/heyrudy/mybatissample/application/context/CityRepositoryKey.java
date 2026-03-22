package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.gateway.CityDbModule.CityRepository;
import com.heyrudy.mybatissample.gateway.CityDbModule.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum CityRepositoryKey
    implements CriticalRepositoryKey<ICityRepository> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, ICityRepository>> lazyLoad() {
        return _ -> Either.right(CityRepository.INSTANCE);
    }

    @Override
    public String toString() {
        return "CityRepositoryKey{}";
    }
}