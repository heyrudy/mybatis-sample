package com.heyrudy.mybatissample.api.controller.rest.interactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.controller.rest.interactor.FindCitiesInteractor;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICitiesFoundDbSPI;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCitiesInteractorTest {

    ICitiesFoundDbSPI mockedGateway = mock(ICitiesFoundDbSPI.class);
    private final FindCitiesInteractor findCitiesInteractor =
        new FindCitiesInteractor(mockedGateway);

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // ARRANGE - precondition or setup
        when(mockedGateway.findCities()).thenReturn(Collections.emptyList());

        // ACT - action or behavior that we are going test
        List<FullCity> actual = findCitiesInteractor.execute();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("find all cities usecase (2)")
    void testFindCities2() {
        // ARRANGE - precondition or setup
        when(mockedGateway.findCities()).thenReturn(List.of(FullCity.builder().build()));

        // ACT - action or behavior that we are going test
        List<FullCity> actual = findCitiesInteractor.execute();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }
}
