package com.heyrudy.mybatissample.rest.service;

import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.exception.ApiRequestException;
import com.heyrudy.mybatissample.repositories.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
public class CityService {

    private final CityRepository repository;
    private final CityMapper mapper;

    @Autowired
    public CityService(CityRepository repository, CityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * @param dto city to persist in database
     */
    public void save(CityDto dto) {
        final String message = "A new city is created";
        repository.insertCity(mapper.cityDtoToCity(dto));
        log.info(message);
    }

    /**
     * @param id city's id to fetch from database
     * @return A required information about a city
     */
    public CityDto findCityById(long id) {
        final String messageRequestSucceed = format("A city with id %d is found", id);
        Optional<CityDto> cityDtoOptional = repository.findCityById(id)
                .map(mapper::cityToCityDto);
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
        final List<CityDto> cityDtoList = repository.findAllCities()
                .stream()
                .map(mapper::cityToCityDto)
                .collect(Collectors.toList());
        log.info(message);
        return cityDtoList;
    }
}
