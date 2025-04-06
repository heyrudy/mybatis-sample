package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper.CityEntityMapper;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public record CityDbAdapter(CityRepository cityRepository)
    implements ICityDbSPI {

    static CityEntityMapper entityMapper = CityEntityMapper.CITY_ENTITY_MAPPER;

    @Override
    public FullCity save(FullCity fullCity) {
        CityEntity cityEntity = entityMapper.toEntity(fullCity);
        CityEntity cityEntitySaved = cityRepository.save(cityEntity);
        return entityMapper.toModel(cityEntitySaved);
    }

    @Override
    public List<FullCity> findCities() {
        return StreamSupport
            .stream(cityRepository.findAll().spliterator(), false)
            .map(entityMapper::toModel)
            .toList();
    }

    @Override
    public Optional<FullCity> findCityById(long id) {
        return cityRepository.findById(id)
            .map(entityMapper::toModel);
    }
}
