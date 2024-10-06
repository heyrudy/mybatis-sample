package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICityFoundByIdDbSPI;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper.CityEntityMapper;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public record CityFoundByIdDbAdapter(CityRepository cityRepository)
    implements ICityFoundByIdDbSPI {

    static CityEntityMapper entityMapper = CityEntityMapper.CITY_ENTITY_MAPPER;

    @Override
    public Optional<FullCity> findCityById(long id) {
        return cityRepository.findById(id)
            .map(entityMapper::toModel);
    }
}
