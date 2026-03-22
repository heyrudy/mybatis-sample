package com.heyrudy.mybatissample.domain;

import java.util.Objects;
import java.util.stream.Stream;

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

        public static FullCity empty() {
            return new FullCity(0L, "", "", "");
        }

        @SafeVarargs
        public static FullCity of(final MutatorStage<FullCity>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    FullCity.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }

        @SafeVarargs
        public final FullCity with(final MutatorStage<FullCity>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    this,
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
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
    }

    record PartialCityProxy(Long id) implements ICity {

        public static PartialCityProxy empty() {
            return new PartialCityProxy(0L);
        }

        @SafeVarargs
        public static PartialCityProxy of(final MutatorStage<PartialCityProxy>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    PartialCityProxy.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }

        @SafeVarargs
        public final PartialCityProxy with(final MutatorStage<PartialCityProxy>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    this,
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
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
    }

    record NullCity(Long id) implements ICity {

        public static NullCity empty() {
            return new NullCity(0L);
        }

        @SafeVarargs
        public static NullCity of(final MutatorStage<NullCity>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    NullCity.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
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
    }

    record CityCriteriaDetails(long cityId) {

        public static CityCriteriaDetails empty() {
            return new CityCriteriaDetails(0L);
        }

        @SafeVarargs
        public static CityCriteriaDetails of(final MutatorStage<CityCriteriaDetails>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    CityCriteriaDetails.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }

        @SafeVarargs
        public final CityCriteriaDetails with(final MutatorStage<CityCriteriaDetails>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(this,
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }
    }

    enum FullCityMutatorStages {
        INSTANCE;

        public MutatorStage<FullCity> id(final Long id) {
            return MutatorStage.of(
                id,
                (it, v) -> new FullCity(v, it.name, it.state, it.country)
            );
        }

        public MutatorStage<FullCity> name(final String name) {
            return MutatorStage.of(
                name,
                (it, v) -> new FullCity(it.id, v, it.state, it.country)
            );
        }

        public MutatorStage<FullCity> state(final String state) {
            return MutatorStage.of(
                state,
                (it, v) -> new FullCity(it.id, it.name, v, it.country)
            );
        }

        public MutatorStage<FullCity> country(final String country) {
            return MutatorStage.of(
                country,
                (it, v) -> new FullCity(it.id, it.name, it.state, v)
            );
        }
    }

    enum PartialCityProxyMutatorStages {
        INSTANCE;

        public MutatorStage<PartialCityProxy> id(final Long id) {
            return MutatorStage.of(
                id,
                (_, v) -> new PartialCityProxy(v)
            );
        }
    }

    enum NullCityMutatorStages {
        INSTANCE;

        public MutatorStage<NullCity> id(final Long id) {
            return MutatorStage.of(
                id,
                (_, v) -> new NullCity(v)
            );
        }
    }

    enum CityCriteriaDetailsMutatorStages {
        INSTANCE;

        public MutatorStage<CityCriteriaDetails> cityId(final Long cityId) {
            return MutatorStage.of(
                cityId,
                (_, v) -> new CityCriteriaDetails(v)
            );
        }
    }
}