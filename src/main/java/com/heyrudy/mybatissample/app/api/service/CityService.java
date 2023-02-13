package com.heyrudy.mybatissample.app.api.service;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityResponseDto;
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
     * @param dto city to persist in the database
     */
    public void save(CityResponseDto dto) {
        final String message = "A new city is created";
        repository.save(mapper.toEntity(dto));
        log.info(message);
    }

    /**
     * @param id city's id to fetch from the database
     * @return A required information about a city
     */
    public CityResponseDto findCityById(long id) {
        Optional<CityResponseDto> cityDtoOptional = repository.findById(id)
                .map(mapper::toDto);
        if (cityDtoOptional.isPresent()) {
            final String messageRequestSucceed = format("A city with id %d is found", id);
            log.info(messageRequestSucceed);
            return cityDtoOptional.get();
        } else {
            final String messageRequestFailed = format("City with %d was not found", id);
            throw new ApiRequestException(messageRequestFailed);
        }
    }

    /**
     * @return All cities fetch from the database
     */
    public List<CityResponseDto> findCities() {
        final List<CityResponseDto> cityResponseDtoList = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(mapper::toDto)
                .toList();
        final String message = "All cities was found";
        log.info(message);
        return cityResponseDtoList;
    }
}
