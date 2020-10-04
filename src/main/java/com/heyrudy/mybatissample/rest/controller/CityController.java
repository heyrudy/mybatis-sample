package com.heyrudy.mybatissample.rest.controller;

import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.rest.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CityController {

    private final CityService service;

    public CityController(CityService service) {
        this.service = service;
    }

    @PostMapping(value = "/cities")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void createCity(@RequestBody CityDto dto) {
        service.save(dto);
    }

    @GetMapping(value = "/cities")
    @ResponseStatus(code = HttpStatus.OK)
    public List<CityDto> findCities() {
        return service.findCities();
    }

    @GetMapping(value = "/cities/{cityId}")
    @ResponseStatus(code = HttpStatus.OK)
    public CityDto findCityById(@PathVariable(value = "cityId") long id) {
        return service.findCityById(id);
    }
}
