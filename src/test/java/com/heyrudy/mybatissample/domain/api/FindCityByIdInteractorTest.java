package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.controller.interactor.CreateCityInteractor;
import com.heyrudy.mybatissample.controller.interactor.FindCityByIdInteractor;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
import io.vavr.control.Either;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCityByIdInteractorTest {

    private static final FindCityByIdInteractor FIND_CITY_BY_ID_INTERACTOR_INSTANCE_UNDER_TEST =
        FindCityByIdInteractor.INSTANCE;

    @AfterEach
    void tearDown() {
        CityRepository.INSTANCE.emptyTable()
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
    }

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
        CreateCityInteractor.INSTANCE.execute(cityToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ACT - action or behavior that we are going to test
        Either<DomainServiceAPIError, ICity> actual =
            FIND_CITY_BY_ID_INTERACTOR_INSTANCE_UNDER_TEST.execute(
                    CityCriteriaDetails.builder().cityId(cityId).build())
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