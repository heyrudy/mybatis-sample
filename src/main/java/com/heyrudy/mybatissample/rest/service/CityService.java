package com.heyrudy.mybatissample.rest.service;

import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.exception.ApiRequestException;
import com.heyrudy.mybatissample.repositories.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        log.info(message);
        repository.insertCity(mapper.cityDtoToCity(dto));
    }

    /**
     * @param id city's id to fetch from database
     * @return A required information about a city
     */
    public CityDto findCityById(long id) {
        final String message = format("A city was found by it's %d id", id);
        log.info(message);
        return repository.findCityById(id)
                .map(mapper::cityToCityDto)
                .orElseThrow(() -> new ApiRequestException(format("City with %d not found", id)));
    }

    /**
     * @return All cities fetch from database
     */
    public List<CityDto> findCities() {
        final String message = "All cities was found";
        log.info(message);
        return repository.findAllCities()
                .stream()
                .map(mapper::cityToCityDto)
                .collect(Collectors.toList());
    }
}
