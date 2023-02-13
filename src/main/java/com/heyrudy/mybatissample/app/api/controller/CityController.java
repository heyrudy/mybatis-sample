package com.heyrudy.mybatissample.app.api.controller;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityResponseDto;
import com.heyrudy.mybatissample.app.api.service.CityService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(value = "/api/v1")
public class CityController {

    CityService service;

    @PostMapping(value = "/cities")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createCity(@RequestBody CityResponseDto dto) {
        service.save(dto);
    }

    @GetMapping(value = "/cities")
    public ResponseEntity<List<CityResponseDto>> findCities() {
        return ResponseEntity.ok()
                .body(service.findCities());
    }

    @GetMapping(value = "/cities/{cityId}")
    public ResponseEntity<CityResponseDto> findCityById(@PathVariable(value = "cityId") long id) {
        return ResponseEntity.ok()
                .body(service.findCityById(id));
    }
}
