package com.heyrudy.mybatissample.entrypoint.rest.dto;

import lombok.Builder;

@Builder
public record CityRequestDTO(String name, String state, String country) {
}