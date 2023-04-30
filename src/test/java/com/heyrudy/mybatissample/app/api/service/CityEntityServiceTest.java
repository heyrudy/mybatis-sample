package com.heyrudy.mybatissample.app.api.service;

import com.heyrudy.mybatissample.api.exception.ApiRequestException;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.CityRepository;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.CityEntity;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityCriteriaDTO;
import com.heyrudy.mybatissample.api.service.CityService;
import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class CityEntityServiceTest {

    private final CityRepository cityRepository = mock(CityRepository.class);
    private final CityService cityService = new CityService(cityRepository);

    @Test
    @DisplayName("find city by id usecase")
    void testFindCityById() {
        // ARRANGE - precondition or setup
        CityEntity expected = CityEntity.builder().build();
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(CityEntity.builder().build()));

        // ACT - action or behaviour that we are going test
        Option<CityEntity> actual = cityService.findCityById(CityCriteriaDTO.builder().cityId(123L).build());

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
                .isNotEmpty()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("find city by id usecase (2)")
    void testFindCityById2() {
        // ARRANGE - precondition or setup
        long expectedId = 123L;
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(CityEntity.builder().build()));

        // ACT - action or behaviour that we are going test

        // ASSERT - verify the result or output using assert statements
        assertThatExceptionOfType(ApiRequestException.class)
                .isThrownBy(() -> cityService.findCityById(CityCriteriaDTO.builder().cityId(expectedId).build()));
    }

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // ARRANGE - precondition or setup
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        // ACT - action or behaviour that we are going test
        Iterable<CityEntity> actual = cityService.findCities();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("find all cities usecase (2)")
    void testFindCities2() {
        // ARRANGE - precondition or setup
        when(cityRepository.findAll()).thenReturn(List.of(CityEntity.builder().build()));

        // ACT - action or behaviour that we are going test
        Iterable<CityEntity> actual = cityService.findCities();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }
}
