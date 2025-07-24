package com.heyrudy.mybatissample.domain;

import java.util.Arrays;
import java.util.Objects;

public interface CityModelModule extends UtilsModule {

    sealed interface ICity
        permits FullCity, PartialCityProxy, NullCity {

        Long getId();

        void setId(Long id);

        String getName();

        String getState();

        String getCountry();
    }

    record FullCity(
        Long id,
        String name,
        String state,
        String country)
        implements ICity {

        public FullCity() {
            this(0L, null, null, null);
        }

        @SafeVarargs
        public static FullCity of(MutatorOption<FullCity>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new FullCity(), (model, option) -> option.apply(model), (a, b) -> a);
        }

        @SafeVarargs
        public final FullCity with(MutatorOption<FullCity>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(this, (model, option) -> option.apply(model), (a, b) -> b);
        }

        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            new FullCity(id, name, state, country);
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public String getCountry() {
            return country;
        }

        public enum FullCityMutatorOptions {
            INSTANCE;

            public MutatorOption<FullCity> id(Long id) {
                return MutatorOption.of(
                    id,
                    (it, v) -> new FullCity(id, it.name, it.state, it.country)
                );
            }

            public MutatorOption<FullCity> name(String name) {
                return MutatorOption.of(
                    name,
                    (it, v) -> new FullCity(it.id, name, it.state, it.country)
                );
            }

            public MutatorOption<FullCity> state(String state) {
                return MutatorOption.of(
                    state,
                    (it, v) -> new FullCity(it.id, it.name, state, it.country)
                );
            }

            public MutatorOption<FullCity> country(String country) {
                return MutatorOption.of(
                    country,
                    (it, v) -> new FullCity(it.id, it.name, it.state, country)
                );
            }
        }
    }

    record PartialCityProxy(Long id) implements ICity {

        public PartialCityProxy() {
            this(0L);
        }

        @SafeVarargs
        public static PartialCityProxy of(MutatorOption<PartialCityProxy>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new PartialCityProxy(), (model, option) -> option.apply(model),
                    (a, b) -> a);
        }

        @SafeVarargs
        public final PartialCityProxy with(MutatorOption<PartialCityProxy>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(this, (model, option) -> option.apply(model), (a, b) -> a);
        }

        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {
            new PartialCityProxy(id);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getState() {
            return null;
        }

        @Override
        public String getCountry() {
            return null;
        }

        public enum PartialCityProxyMutatorOptions {
            INSTANCE;

            public MutatorOption<PartialCityProxy> id(Long id) {
                return MutatorOption.of(
                    id,
                    (it, v) -> new PartialCityProxy(id)
                );
            }
        }
    }

    record NullCity(Long id) implements ICity {

        public NullCity() {
            this(0L);
        }

        @SafeVarargs
        public static NullCity of(MutatorOption<NullCity>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new NullCity(), (model, option) -> option.apply(model),
                    (a, b) -> a);
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            new NullCity(id);
        }

        @Override
        public String getName() {
            return "No name";
        }

        @Override
        public String getState() {
            return "No state";
        }

        @Override
        public String getCountry() {
            return "No country";
        }

        public enum NullCityMutatorOptions {
            INSTANCE;

            public MutatorOption<NullCity> id(Long id) {
                return MutatorOption.of(
                    id,
                    (it, v) -> new NullCity(id)
                );
            }
        }
    }

    record CityCriteriaDetails(long cityId) {

        public CityCriteriaDetails() {
            this(0L);
        }

        @SafeVarargs
        public static CityCriteriaDetails of(MutatorOption<CityCriteriaDetails>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new CityCriteriaDetails(), (model, option) -> option.apply(model),
                    (a, b) -> a);
        }

        @SafeVarargs
        public final CityCriteriaDetails with(MutatorOption<CityCriteriaDetails>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(this, (model, option) -> option.apply(model), (a, b) -> a);
        }

        public enum CityCriteriaDetailsMutatorOptions {
            INSTANCE;

            public MutatorOption<CityCriteriaDetails> cityId(Long cityId) {
                return MutatorOption.of(
                    cityId,
                    (it, v) -> new CityCriteriaDetails(cityId)
                );
            }
        }
    }
}
