package com.heyrudy.mybatissample.api.controller.rest.interactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.controller.rest.interactor.CreateCityInteractor;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICityRegisterDbSPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateCityInteractorTest {

    ICityRegisterDbSPI mockedGateway = mock(ICityRegisterDbSPI.class);
    private final CreateCityInteractor createCityInteractor =
        new CreateCityInteractor(mockedGateway);

    @Test
    @DisplayName("create city usecase")
    void testCreateCity() {
        // ARRANGE - precondition or setup
        FullCity expected = FullCity.builder()
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();
        when(mockedGateway.save(isA(FullCity.class))).thenReturn(expected);

        // ACT - action or behavior that we are going test
        FullCity actual = createCityInteractor.execute(expected);

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isNotNull()
            .satisfies(it ->
                assertThat(it)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            );
    }
}
