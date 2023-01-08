package com.heyrudy.mybatissample.app.api.service;

import com.heyrudy.mybatissample.app.api.exception.ApiRequestException;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.CityRepository;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityDto;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper.CityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CityService.class, CityMapper.class, CityRepository.class})
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
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));
        // Act
        // Assert
        assertThat(cityService.findCityById(123L))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("find city by id usecase (2)")
    void testFindCityById2() {
        // Arrange
        long cityId = 123L;
        City city = new City();
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(null);
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));
        // Act
        // Assert
        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> cityService.findCityById(cityId));
    }

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // Arrange
        ArrayList<City> cities = new ArrayList<>();
        when(cityRepository.findAll()).thenReturn(cities);
        // Act
        // Assert
        assertThat(cityService.findCities())
                .isNotNull()
                .isEmpty();
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
        when(cityRepository.findAll()).thenReturn(cities);
        // Act
        // Assert
        assertThat( cityService.findCities())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }
}
