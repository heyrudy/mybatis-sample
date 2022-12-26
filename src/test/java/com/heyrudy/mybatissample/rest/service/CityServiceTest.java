package com.heyrudy.mybatissample.rest.service;

import com.heyrudy.mybatissample.domain.City;
import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.exception.ApiRequestException;
import com.heyrudy.mybatissample.repositories.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CityService.class, CityMapper.class, CityRepository.class})
@ExtendWith(SpringExtension.class)
class CityServiceTest {

    @MockBean
    private CityMapper cityMapper;
    @MockBean
    private CityRepository cityRepository;
    @Autowired
    private CityService cityService;

    @Test
    @DisplayName("find city by id usecase")
    void testFindCityById() {
        // Arrange
        CityDto expected = new CityDto();
        City city = new City();
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(expected);
        when(cityRepository.findCityById(anyLong())).thenReturn(Optional.of(city));
        // Act
        // Assert
        assertSame(expected, cityService.findCityById(123L));
    }

    @Test
    @DisplayName("find city by id usecase (2)")
    void testFindCityById2() {
        // Arrange
        long cityId = 123L;
        City city = new City();
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(null);
        when(cityRepository.findCityById(anyLong())).thenReturn(Optional.of(city));
        // Act
        // Assert
        assertThrows(ApiRequestException.class, () -> cityService.findCityById(cityId));
    }

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // Arrange
        ArrayList<City> cities = new ArrayList<>();
        when(cityRepository.findAllCities()).thenReturn(cities);
        // Act
        // Assert
        assertEquals(0, cityService.findCities().size());
    }

    @Test
    @DisplayName("find all cities usecase (2)")
    void testFindCities2() {
        // Arrange
        CityDto cityDto = new CityDto();
        City city = new City();
        ArrayList<City> cities = new ArrayList<>();
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(cityDto);
        cities.add(city);
        when(cityRepository.findAllCities()).thenReturn(cities);
        // Act
        // Assert
        assertEquals(1, cityService.findCities().size());
    }
}
