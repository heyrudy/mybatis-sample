package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import com.heyrudy.mybatissample.gateway.db.relational.repository.MockedCityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum MockedCityRepositoryKey
    implements CriticalRepositoryKey<ICityRepository> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<? extends MissingCriticalDependencyError, ICityRepository>> describeDependencyContext() {
        return __ ->
            Either.right(MockedCityRepository.INSTANCE);
    }

    @Override
    public String toString() {
        return "MockedCityRepositoryKey{}";
    }
}