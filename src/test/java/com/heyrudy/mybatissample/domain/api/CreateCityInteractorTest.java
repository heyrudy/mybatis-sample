package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
import com.heyrudy.mybatissample.controller.interactor.CreateCityInteractor;
import io.vavr.control.Either;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateCityInteractorTest {

    private static final CreateCityInteractor createCityInteractorInstanceUnderTest =
        CreateCityInteractor.INSTANCE;

    @AfterEach
    void tearDown() {
        CityRepository.INSTANCE.emptyTable()
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
    }

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        ICity cityToSave =
            FullCity.builder()
                .name("Paris")
                .country("France")
                .state("Paris75").build();

        // ACT - action or behavior that we are going to test
        Either<DomainServiceAPIError, ICity> actual =
            createCityInteractorInstanceUnderTest.execute(cityToSave)
                .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(iCity -> {
                    assertThat(iCity.getName())
                        .isEqualTo(cityToSave.getName());
                    assertThat(iCity.getState())
                        .isEqualTo(cityToSave.getState());
                    assertThat(iCity.getCountry())
                        .isEqualTo(cityToSave.getCountry());
                }
            );
    }
}