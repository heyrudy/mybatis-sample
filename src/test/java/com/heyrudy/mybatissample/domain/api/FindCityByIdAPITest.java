package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import io.vavr.control.Either;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCityByIdAPITest {

    private final FindCityByIdAPI findCityByIdAPIInstanceUnderTest = FindCityByIdAPI.INSTANCE;

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        ICity cityToSave =
            FullCity.builder()
                .name("Paris")
                .state("Paris75")
                .country("France").build();
        CreateCityAPI.INSTANCE.execute(cityToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ACT - action or behavior that we are going to test
        Either<DomainServiceAPIError, ICity> actual =
            findCityByIdAPIInstanceUnderTest.execute(
                    CityCriteriaDetails.builder().cityId(cityId).build())
                .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(iCity ->
                assertThat(iCity)
                    .usingRecursiveComparison()
                    .isEqualTo(cityToSave)
            );
    }
}