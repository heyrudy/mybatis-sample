package com.heyrudy.mybatissample.api.controller.dto;

import lombok.*;

@Builder
public record CityResponseDTO(String name, String state, String country) {
}