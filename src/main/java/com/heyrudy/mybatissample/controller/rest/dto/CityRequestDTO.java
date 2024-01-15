package com.heyrudy.mybatissample.controller.rest.dto;

public record CityRequestDTO(String name, String state, String country) {

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
