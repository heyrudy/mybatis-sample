package com.heyrudy.mybatissample.app.api.service;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityDto;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.app.api.exception.ApiRequestException;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.CityRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class CityService {

    CityRepository repository;
    CityMapper mapper;

    /**
     * @param dto city to persist in database
     */
    public void save(CityDto dto) {
        final String message = "A new city is created";
        repository.save(mapper.toCity(dto));
        log.info(message);
    }

    /**
     * @param id city's id to fetch from database
     * @return A required information about a city
     */
    public CityDto findCityById(long id) {
        final String messageRequestSucceed = format("A city with id %d is found", id);
        Optional<CityDto> cityDtoOptional = repository.findById(id)
                .map(mapper::toCityDto);
        if (cityDtoOptional.isPresent()) {
            log.info(messageRequestSucceed);
            return cityDtoOptional.get();
        } else {
            final String messageRequestFailed = format("City with %d was not found", id);
            throw new ApiRequestException(messageRequestFailed);
        }
    }

    /**
     * @return All cities fetch from database
     */
    public List<CityDto> findCities() {
        final String message = "All cities was found";
        final List<CityDto> cityDtoList = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(mapper::toCityDto)
                .toList();
        log.info(message);
        return cityDtoList;
    }
}
