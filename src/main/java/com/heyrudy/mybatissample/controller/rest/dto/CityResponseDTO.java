package com.heyrudy.mybatissample.controller.rest.dto;

public record CityResponseDTO(String name, String state, String country) {

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
