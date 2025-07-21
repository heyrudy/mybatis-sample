package com.heyrudy.mybatissample.application.interactor;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.application.CityInteractorModule.CreateCityInteractor;
import com.heyrudy.mybatissample.application.CityInteractorModule.FindCitiesInteractor;
import com.heyrudy.mybatissample.application.CityInteractorModule.FindCityByIdInteractor;
import com.heyrudy.mybatissample.domain.CityModelModule.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.CityModelModule.CityCriteriaDetails.CityCriteriaDetailsMutatorOptions;
import com.heyrudy.mybatissample.domain.CityModelModule.FullCity;
import com.heyrudy.mybatissample.domain.CityModelModule.FullCity.FullCityMutatorOptions;
import com.heyrudy.mybatissample.domain.CityModelModule.ICity;
import com.heyrudy.mybatissample.domain.CityModelModule.NullCity;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.DomainErrorModule.DomainServiceAPIError;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.db.CityDbModule.CityRepository;
import io.vavr.control.Either;
import java.util.List;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityInteractorModuleTest {

    private static final CreateCityInteractor CREATE_CITY_INTERACTOR_INSTANCE_UNDER_TEST =
        CreateCityInteractor.INSTANCE;
    private static final FindCityByIdInteractor FIND_CITY_BY_ID_INTERACTOR_INSTANCE_UNDER_TEST =
        FindCityByIdInteractor.INSTANCE;
    private static final FindCitiesInteractor FIND_CITIES_INTERACTOR_INSTANCE_UNDER_TEST =
        FindCitiesInteractor.INSTANCE;

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
            FullCity.with(
                FullCityMutatorOptions.INSTANCE.name("Paris"),
                FullCityMutatorOptions.INSTANCE.state("France"),
                FullCityMutatorOptions.INSTANCE.country("Paris75"));

        // ACT - action or behavior that we are going to test
        Either<DomainErrorModule.DomainServiceAPIError, ICity> actual =
            CREATE_CITY_INTERACTOR_INSTANCE_UNDER_TEST.execute(cityToSave)
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

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        ICity cityToSave =
            FullCity.with(
                FullCityMutatorOptions.INSTANCE.name("Paris"),
                FullCityMutatorOptions.INSTANCE.state("Paris75"),
                FullCityMutatorOptions.INSTANCE.country("France"));
        CreateCityInteractor.INSTANCE.execute(cityToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ACT - action or behavior that we are going to test
        Either<DomainErrorModule.DomainServiceAPIError, ICity> actual =
            FIND_CITY_BY_ID_INTERACTOR_INSTANCE_UNDER_TEST.execute(
                    CityCriteriaDetails.with(
                        CityCriteriaDetailsMutatorOptions.INSTANCE.cityId(cityId)))
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

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // ARRANGE - precondition or setup
        ICity cityZeroToSave =
            NullCity.INSTANCE;
        ICity cityOneToSave =
            FullCity.with(
                FullCityMutatorOptions.INSTANCE.id(1L),
                FullCityMutatorOptions.INSTANCE.name("Paris"),
                FullCityMutatorOptions.INSTANCE.state("France"),
                FullCityMutatorOptions.INSTANCE.country("Paris75"));
        CreateCityInteractor.INSTANCE.execute(cityZeroToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
        CreateCityInteractor.INSTANCE.execute(cityOneToSave)
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);

        // ACT - action or behavior that we are going to test
        Either<DomainServiceAPIError, List<ICity>> actual =
            FIND_CITIES_INTERACTOR_INSTANCE_UNDER_TEST.execute()
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