package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.service.AttractionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class AttractionsController {
    private final AttractionsService attractionsService;

    public AttractionsController(AttractionsService attractionsService) {
        this.attractionsService = attractionsService;
    }

    @GetMapping("/attractions")
    public ResponseEntity<List<Attractions>> getAll() {
        List<Attractions> attractionsList = attractionsService.getAll();
        return new ResponseEntity<>(attractionsList, HttpStatus.OK);
    }

    @GetMapping("/attractions/{id}")
    public ResponseEntity<Attractions> getAttractionsId(@PathVariable("id") Long id) {
        Optional<Attractions> attractions = attractionsService.getAttractionsId(id);
        if (attractions.isPresent()) {
            return new ResponseEntity<>(attractions.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping // создает
    public ResponseEntity<HttpStatus> create(@RequestBody Attractions attractions){
        return new ResponseEntity<>( attractionsService.createAttraction(attractions)?HttpStatus.CREATED : HttpStatus.CONFLICT);
    }
}
