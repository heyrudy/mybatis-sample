package com.heyrudy.mybatissample.api.service;

import com.heyrudy.mybatissample.controller.rest.dto.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.api.CityServiceAPI;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.interactor.CreateCity;
import com.heyrudy.mybatissample.domain.interactor.FindCities;
import com.heyrudy.mybatissample.domain.interactor.FindCityById;
import io.vavr.control.Either;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CityServiceAPITest {

    private final CreateCity createCity = mock(CreateCity.class);
    private final FindCityById findCityById = mock(FindCityById.class);
    private final FindCities findCities = mock(FindCities.class);
    private final CityServiceAPI cityServiceAPI = new CityServiceAPI(createCity, findCityById, findCities);

    @Test
    @DisplayName("find city by id usecase")
    void testFindCityById() {
        // ARRANGE - precondition or setup
        City expected = City.builder().build();
        CityCriteriaDTO cityCriteria = CityCriteriaDTO.builder().cityId(123L).build();
        when(findCityById.execute(anyLong())).thenReturn(Optional.of(City.builder().build()));

        // ACT - action or behavior that we are going test
        Either<CityNotFoundError, City> actual = cityServiceAPI.findCityById(cityCriteria);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
                .isNotNull()
                .isRight()
                .containsRightInstanceOf(City.class)
                .hasRightValueSatisfying(it ->
                        assertThat(it)
                                .usingRecursiveComparison()
                                .isEqualTo(expected)
                );
    }

    @Test
    @DisplayName("find city by id usecase (2)")
    void testFindCityById2() {
        // ARRANGE - precondition or setup
        long expectedId = 123L;
        CityCriteriaDTO cityCriteria = CityCriteriaDTO.builder().cityId(expectedId).build();
        when(findCityById.execute(anyLong())).thenReturn(Optional.empty());

        // ACT - action or behavior that we are going test
        Either<CityNotFoundError, City> actual = cityServiceAPI.findCityById(cityCriteria);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
                .isNotNull()
                .isLeft()
                .containsLeftInstanceOf(CityNotFoundError.class)
                .hasLeftValueSatisfying(it ->
                        assertThat(it)
                                .extracting(CityNotFoundError::getMessage)
                                .isEqualTo(format("City with %d was not found", cityCriteria.cityId()))
                );
    }

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // ARRANGE - precondition or setup
        when(findCities.execute()).thenReturn(Collections.emptyList());

        // ACT - action or behavior that we are going test
        List<City> actual = cityServiceAPI.findCities();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("find all cities usecase (2)")
    void testFindCities2() {
        // ARRANGE - precondition or setup
        when(findCities.execute()).thenReturn(List.of(City.builder().build()));

        // ACT - action or behavior that we are going test
        List<City> actual = cityServiceAPI.findCities();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }
}
