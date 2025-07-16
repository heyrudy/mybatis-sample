package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.gateway.db.CityDbModule.CityRepository;
import com.heyrudy.mybatissample.gateway.db.CityDbModule.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum CityRepositoryKey
    implements CriticalRepositoryKey<ICityRepository> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, ICityRepository>> lazyLoad() {
        return __ -> Either.right(CityRepository.INSTANCE);
    }

    @Override
    public String toString() {
        return "CityRepositoryKey{}";
    }
}