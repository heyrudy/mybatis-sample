package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICitiesFoundDbSPI;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper.CityEntityMapper;
import java.util.List;
import java.util.stream.StreamSupport;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public record CitiesFoundDbAdapter(CityRepository cityRepository)
    implements ICitiesFoundDbSPI {

    static CityEntityMapper entityMapper = CityEntityMapper.CITY_ENTITY_MAPPER;

    @Override
    public List<FullCity> findCities() {
        return StreamSupport
            .stream(cityRepository.findAll().spliterator(), false)
            .map(entityMapper::toModel)
            .toList();
    }
}
