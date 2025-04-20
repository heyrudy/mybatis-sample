package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CityRepositoryKey;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper.CityEntityMapper;
import cyclops.control.Reader;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class CityCriticalDbAdapter implements ICityDbSPI {

    public static final CityEntityMapper CITY_ENTITY_MAPPER = CityEntityMapper.CITY_ENTITY_MAPPER;

    @Override
    public Reader<AppScopedServiceLocator, Either<CriticalRepositoryNotFoundByLocatorError, ICity>> save(
        ICity fullCity) {
        return locator ->
            locator.getCriticalRepository(CityRepositoryKey.INSTANCE)
                .bimap(
                    Function.identity(),
                    cityRepository -> {
                        CityEntity cityEntity = CITY_ENTITY_MAPPER.toEntity(fullCity);
                        CityEntity cityEntitySaved = cityRepository.save(cityEntity);
                        return CITY_ENTITY_MAPPER.toModel(cityEntitySaved);
                    }
                );
    }

    @Override
    public Reader<AppScopedServiceLocator, Either<CriticalRepositoryNotFoundByLocatorError, List<ICity>>> findCities() {
        return locator ->
            locator.getCriticalRepository(CityRepositoryKey.INSTANCE)
                .bimap(
                    Function.identity(),
                    cityRepository ->
                        Stream.ofAll(cityRepository.findAll())
                            .map(CITY_ENTITY_MAPPER::toModel)
                            .toJavaList()
                );
    }

    @Override
    public Reader<AppScopedServiceLocator, Either<CriticalRepositoryNotFoundByLocatorError, Optional<ICity>>> findCityById(
        long id) {
        return locator ->
            locator.getCriticalRepository(CityRepositoryKey.INSTANCE)
                .bimap(
                    Function.identity(),
                    cityRepository ->
                        cityRepository.findById(id)
                            .map(CITY_ENTITY_MAPPER::toModel)
                );
    }
}
