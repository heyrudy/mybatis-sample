package com.heyrudy.mybatissample.application;

public interface CityDTOModule {

    record CityResponseDTO(String name, String state, String country) {

        public static CityResponseDTOBuilder builder() {
            return new CityResponseDTOBuilder();
        }

        public static class CityResponseDTOBuilder {

            private String name;
            private String state;
            private String country;

            public CityResponseDTOBuilder() {
                super();
            }

            public CityResponseDTOBuilder name(String name) {
                this.name = name;
                return this;
            }

            public CityResponseDTOBuilder state(String state) {
                this.state = state;
                return this;
            }

            public CityResponseDTOBuilder country(String country) {
                this.country = country;
                return this;
            }

            public CityResponseDTO build() {
                return new CityResponseDTO(name, state, country);
            }
        }
    }

    record CityRequestDTO(String name, String state, String country) {

        public static CityRequestDTOBuilder builder() {
            return new CityRequestDTOBuilder();
        }

        public static class CityRequestDTOBuilder {

            private String name;
            private String state;
            private String country;

            public CityRequestDTOBuilder() {
                super();
            }

            public CityRequestDTOBuilder name(String name) {
                this.name = name;
                return this;
            }

            public CityRequestDTOBuilder state(String state) {
                this.state = state;
                return this;
            }

            public CityRequestDTOBuilder country(String country) {
                this.country = country;
                return this;
            }

            public CityRequestDTO build() {
                return new CityRequestDTO(name, state, country);
            }
        }
    }
}
