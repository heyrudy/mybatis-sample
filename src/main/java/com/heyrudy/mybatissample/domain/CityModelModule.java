package com.heyrudy.mybatissample.domain;

import java.util.Arrays;
import java.util.Objects;

public interface CityModelModule extends UtilsModule {

    final class FullCity implements ICity {

        private Long id;
        private String name;
        private String state;
        private String country;

        public FullCity() {
            super();
        }

        @SafeVarargs
        public static FullCity with(MutatorOption<FullCity>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new FullCity(), (model, option) -> option.apply(model), (a, b) -> a);
        }

        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
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
                    (it, v) -> {
                        it.id = id;
                        return it;
                    });
            }

            public MutatorOption<FullCity> name(String name) {
                return MutatorOption.of(
                    name,
                    (it, v) -> {
                        it.name = name;
                        return it;
                    });
            }

            public MutatorOption<FullCity> state(String state) {
                return MutatorOption.of(
                    state,
                    (it, v) -> {
                        it.state = state;
                        return it;
                    });
            }

            public MutatorOption<FullCity> country(String country) {
                return MutatorOption.of(
                    country,
                    (it, v) -> {
                        it.country = country;
                        return it;
                    });
            }
        }
    }

    final class PartialCityProxy implements ICity {

        private Long id;

        public PartialCityProxy() {
            super();
        }

        @SafeVarargs
        public static PartialCityProxy with(MutatorOption<PartialCityProxy>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new PartialCityProxy(), (model, option) -> option.apply(model), (a, b) -> a);
        }

        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
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
                    (it, v) -> {
                        it.id = id;
                        return it;
                    });
            }
        }
    }

    sealed interface ICity
        permits FullCity, NullCity, PartialCityProxy {

        Long getId();

        void setId(Long id);

        String getName();

        String getState();

        String getCountry();
    }

    final class NullCity implements ICity {

        public static final NullCity INSTANCE = new NullCity();

        private Long id;

        public NullCity() {
            super();
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
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
    }

    record CityCriteriaDetails(long cityId) {

        public CityCriteriaDetails() {
            this(0L);
        }

        @SafeVarargs
        public static CityCriteriaDetails with(MutatorOption<CityCriteriaDetails>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new CityCriteriaDetails(), (model, option) -> option.apply(model), (a, b) -> a);
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
