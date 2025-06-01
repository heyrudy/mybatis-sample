package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.controller.interactor.CreateCityInteractor;
import com.heyrudy.mybatissample.controller.interactor.FindCitiesInteractor;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.city.NullCity;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
import io.vavr.control.Either;
import java.util.List;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCitiesInteractorTest {

    private static final FindCitiesInteractor findCitiesInteractorInstanceUnderTest =
        FindCitiesInteractor.INSTANCE;

    @AfterEach
    void tearDown() {
        CityRepository.INSTANCE.emptyTable()
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // ARRANGE - precondition or setup
        ICity cityZeroToSave =
            NullCity.builder().build();
        ICity cityOneToSave =
            FullCity.builder()
                .id(1L)
                .name("Paris")
                .country("France")
                .state("Paris75").build();
        CreateCityInteractor.INSTANCE.execute(cityZeroToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
        CreateCityInteractor.INSTANCE.execute(cityOneToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ACT - action or behavior that we are going to test
        Either<DomainServiceAPIError, List<ICity>> actual =
            findCitiesInteractorInstanceUnderTest.execute()
                .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(cities -> {
                assertThat(cities)
                    .isNotEmpty()
                    .hasSize(2);
                assertThat(cities.get(0).getId())
                    .isEqualTo(1L);
                assertThat(cities.get(1).getId())
                    .isEqualTo(2L);
                assertThat(cities.get(1).getName())
                    .isEqualTo("Paris");
            });
    }
}