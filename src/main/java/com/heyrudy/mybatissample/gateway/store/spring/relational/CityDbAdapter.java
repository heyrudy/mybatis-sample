package com.heyrudy.mybatissample.gateway.store.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.spi.store.ICityDbSPI;
import com.heyrudy.mybatissample.gateway.store.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.store.spring.relational.entity.mapper.CityEntityMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CityDbAdapter implements ICityDbSPI {

    CityRepository cityRepository;
    static CityEntityMapper entityMapper = CityEntityMapper.CITY_ENTITY_MAPPER;

    @Override
    public City save(City city) {
        CityEntity cityEntity = entityMapper.toEntity(city);
        CityEntity cityEntitySaved = cityRepository.save(cityEntity);
        return entityMapper.toModel(cityEntitySaved);
    }

    @Override
    public Optional<City> findCityById(long id) {
        return cityRepository.findById(id)
                .map(entityMapper::toModel);
    }


    @Override
    public List<City> findCities() {
        return StreamSupport
                .stream(cityRepository.findAll().spliterator(), false)
                .map(entityMapper::toModel)
                .toList();
    }
}
