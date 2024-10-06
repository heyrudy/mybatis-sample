package com.heyrudy.mybatissample.api.controller.rest.interactor;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.controller.rest.interactor.FindCityByIdInteractor;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.spi.ICityFoundByIdDbSPI;
import io.vavr.control.Either;
import java.util.Optional;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCityByIdInteractorTest {

    ICityFoundByIdDbSPI mockedGateway = mock(ICityFoundByIdDbSPI.class);
    private final FindCityByIdInteractor findCityByIdInteractor =
        new FindCityByIdInteractor(mockedGateway);

    @Test
    @DisplayName("find city by id usecase")
    void testFindCityById() {
        // ARRANGE - precondition or setup
        FullCity expected = FullCity.builder().build();
        CityCriteriaDTO cityCriteria = CityCriteriaDTO.builder().cityId(123L).build();
        when(mockedGateway.findCityById(anyLong())).thenReturn(Optional.of(FullCity.builder().build()));

        // ACT - action or behavior that we are going test
        Either<CityNotFoundError, FullCity> actual = findCityByIdInteractor.execute(cityCriteria);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .containsRightInstanceOf(FullCity.class)
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
        when(mockedGateway.findCityById(anyLong())).thenReturn(Optional.empty());

        // ACT - action or behavior that we are going test
        Either<CityNotFoundError, FullCity> actual = findCityByIdInteractor.execute(cityCriteria);

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
