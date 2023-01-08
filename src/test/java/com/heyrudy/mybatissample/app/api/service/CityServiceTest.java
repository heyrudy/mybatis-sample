package com.heyrudy.mybatissample.app.api.service;

import com.heyrudy.mybatissample.app.api.exception.ApiRequestException;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.CityRepository;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityDto;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper.CityMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CityServiceTest {

    @InjectMocks
    private CityService cityService;
    @Mock
    private CityMapper cityMapper;
    @Mock
    private CityRepository cityRepository;

    @Test
    @DisplayName("find city by id usecase")
    void testFindCityById() {
        // Arrange
        CityDto expected = CityDto.builder().build();
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(expected);
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(City.builder().build()));
        // Act
        // Assert
        assertThat(cityService.findCityById(123L))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("find city by id usecase (2)")
    void testFindCityById2() {
        // Arrange
        long expectedId = 123L;
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(null);
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(City.builder().build()));
        // Act
        // Assert
        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> cityService.findCityById(expectedId));
    }

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // Arrange
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());
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
        when(cityMapper.toCityDto(or(isA(City.class), isNull()))).thenReturn(CityDto.builder().build());
        when(cityRepository.findAll()).thenReturn(List.of(City.builder().build()));
        // Act
        // Assert
        assertThat( cityService.findCities())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }
}
