package com.heyrudy.mybatissample.api.service;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.api.FindCityByIdInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import io.vavr.control.Either;
import java.util.Optional;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCityByIdInteractorAPITest {

    ICityDbSPI db = mock(ICityDbSPI.class);
    private final FindCityByIdInteractorAPI findCityByIdInteractorAPI = new FindCityByIdInteractorAPI(db);

    @Test
    @DisplayName("find city by id usecase")
    void testFindCityById() {
        // ARRANGE - precondition or setup
        City expected = City.builder().build();
        CityCriteriaDTO cityCriteria = CityCriteriaDTO.builder().cityId(123L).build();
        when(db.findCityById(anyLong())).thenReturn(Optional.of(City.builder().build()));

        // ACT - action or behavior that we are going test
        Either<CityNotFoundError, City> actual = findCityByIdInteractorAPI.findCityById(cityCriteria);

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
        when(db.findCityById(anyLong())).thenReturn(Optional.empty());

        // ACT - action or behavior that we are going test
        Either<CityNotFoundError, City> actual = findCityByIdInteractorAPI.findCityById(cityCriteria);

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
}
