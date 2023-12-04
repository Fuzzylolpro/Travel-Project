package com.example.travelproject.controller;

import com.example.travelproject.domain.City;
import com.example.travelproject.service.CityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/city")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }


    @GetMapping
    public ResponseEntity<List<City>> getAll() {
        List<City> cityList = cityService.getAll();
        return new ResponseEntity<>(cityList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityId(@PathVariable("id") Long id) {
        Optional<City> city = cityService.getCityId(id);
        if (city.isPresent()) {
            return new ResponseEntity<>(city.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping // создает
    public ResponseEntity<HttpStatus> create(@RequestBody City city) {
        return new ResponseEntity<>(cityService.createCity(city) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        cityService.deleteCityById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody City city) {
        return new ResponseEntity<>(cityService.updateCity(city) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}