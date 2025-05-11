package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedError;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import io.vavr.control.Either;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateCityAPITest {

    private final CreateCityAPI createCityAPIInstanceUnderTest = CreateCityAPI.INSTANCE;

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        ICity cityToSave = FullCity.builder()
            .name("Paris")
            .country("France")
            .state("Paris75").build();

        // ACT - action or behavior that we are going to test
        Either<CityNotSavedError, ICity> actual =
            createCityAPIInstanceUnderTest.execute(cityToSave)
                .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .usingRecursiveComparison()
            .isEqualTo(cityToSave);
    }
}