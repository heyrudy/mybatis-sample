package com.heyrudy.mybatissample.controller.rest.dto;

import lombok.*;

@Builder
public record CityResponseDTO(String name, String state, String country) {
}
