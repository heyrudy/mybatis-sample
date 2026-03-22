package com.heyrudy.mybatissample.application.interactor;

import com.heyrudy.mybatissample.domain.CityModelModule.FullCity;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Provide;

class Generators {

    @Provide
    static Arbitrary<FullCity> cities() {
        Arbitrary<String> name =
            Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(10);
        Arbitrary<String> state =
            Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(10);
        Arbitrary<String> country =
            Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(10);
        Arbitrary<Long> id =
            Arbitraries.longs()
                .between(0, 1_000_000);

        return Combinators.combine(
            id,
            name,
            state,
            country
        ).as(FullCity::new);
    }
}