package com.heyrudy.mybatissample.gateway.db.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICityRegisterDbSPI;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper.CityEntityMapper;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public record CityRegisterDbAdapter(CityRepository cityRepository)
    implements ICityRegisterDbSPI {

    static CityEntityMapper entityMapper = CityEntityMapper.CITY_ENTITY_MAPPER;

    @Override
    public FullCity save(FullCity fullCity) {
        CityEntity cityEntity = entityMapper.toEntity(fullCity);
        CityEntity cityEntitySaved = cityRepository.save(cityEntity);
        return entityMapper.toModel(cityEntitySaved);
    }
}
