package com.heyrudy.mybatissample.application.interactor;

import com.heyrudy.mybatissample.application.CityInteractorModule.CreateCityInteractor;
import com.heyrudy.mybatissample.domain.CityModelModule.ICity;
import com.heyrudy.mybatissample.domain.DomainErrorModule.DomainServiceAPIError;
import com.heyrudy.mybatissample.gateway.CityDbModule.CityRepository;
import com.heyrudy.mybatissample.gateway.config.TestConfig.SpringTestAppScopedDependencyLocator;
import io.vavr.control.Either;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;

class CityInteractorOperationTests {

    @AfterEach
    void tearDown() {
        CityRepository.INSTANCE.emptyTable()
            .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
    }

    @Property
    void insertProducesPersist(@ForAll("cities") ICity incoming) {
        Either<DomainServiceAPIError, ICity> result =
            CreateCityInteractor.INSTANCE.execute(incoming)
                .apply(SpringTestAppScopedDependencyLocator.INSTANCE);
        Assertions.assertThat(result).isNotNull();
    }
}