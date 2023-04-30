package com.heyrudy.mybatissample.api.service;

import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.CityRepository;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.CityEntity;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityCriteriaDTO;
import com.heyrudy.mybatissample.core.error.CityNotFoundError;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CityService {

    CityRepository repository;

    /**
     * @param entity city to persist in the database
     * @return Persisted city in the database
     */
    public CityEntity createCity(final CityEntity entity) {
        return repository.save(entity);
    }

    /**
     * @param cityCriteriaDTO city's id to fetch from the database
     * @return Required information about a city
     */
    public Option<CityEntity> findCityById(final CityCriteriaDTO cityCriteriaDTO) {
        return Option.ofOptional(repository.findById(cityCriteriaDTO.cityId()))
                .onEmpty(() -> new CityNotFoundError(format("City with %d was not found", cityCriteriaDTO.cityId())));

    }

    /**
     * @return All cities fetch from the database
     */
    public Iterable<CityEntity> findCities() {
        return repository.findAll();
    }
}
