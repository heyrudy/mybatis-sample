package com.heyrudy.mybatissample.domain.model;

public interface CityModelModule {

    final class FullCity implements ICity {

        private Long id;
        private String name;
        private String state;
        private String country;

        public FullCity() {
            super();
        }

        public static FullCity builder() {
            return new FullCity();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public FullCity id(Long id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public FullCity name(String name) {
            this.name = name;
            return this;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public FullCity state(String state) {
            this.state = state;
            return this;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public FullCity country(String country) {
            this.country = country;
            return this;
        }

        public FullCity build() {
            return this;
        }
    }

    final class PartialCityProxy implements ICity {

        private Long id;
        private String name;
        private String state;
        private String country;

        public PartialCityProxy() {
            super();
        }

        public static PartialCityProxy builder() {
            return new PartialCityProxy();
        }

        public PartialCityProxy(Long id, String name, String country) {
            super();
            this.id = id;
            this.name = name;
            this.country = country;
        }

        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        public PartialCityProxy id(long id) {
            this.id = id;
            return this;
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

        public PartialCityProxy build() {
            return this;
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

        private Long id;

        public NullCity() {
            super();
        }

        public static NullCity builder() {
            return new NullCity();
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

        public NullCity build() {
            return this;
        }
    }

    record CityCriteriaDetails(long cityId) {

        public static CityCriteriaDTOBuilder builder() {
            return new CityCriteriaDTOBuilder();
        }

        public static class CityCriteriaDTOBuilder {

            private long cityId;

            public CityCriteriaDTOBuilder() {
            }

            public CityCriteriaDTOBuilder cityId(long cityId) {
                this.cityId = cityId;
                return this;
            }

            public CityCriteriaDetails build() {
                return new CityCriteriaDetails(cityId);
            }
        }
    }
}
