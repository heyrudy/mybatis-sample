package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.gateway.db.repository.ICityRepository;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
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