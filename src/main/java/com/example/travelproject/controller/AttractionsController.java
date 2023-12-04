package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.service.AttractionsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/attractions")
public class AttractionsController {
    private final AttractionsService attractionsService;

    public AttractionsController(AttractionsService attractionsService) {
        this.attractionsService = attractionsService;
    }

    @GetMapping
    public ResponseEntity<List<Attractions>> getAll() {
        List<Attractions> attractionsList = attractionsService.getAll();
        return new ResponseEntity<>(attractionsList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attractions> getAttractionsId(@PathVariable("id") Long id) {
        Optional<Attractions> attractions = attractionsService.getAttractionsId(id);
        if (attractions.isPresent()) {
            return new ResponseEntity<>(attractions.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody Attractions attractions) {
        return new ResponseEntity<>(attractionsService.createAttraction(attractions) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        attractionsService.deleteAttractionsById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody Attractions attractions) {
        return new ResponseEntity<>(attractionsService.updateAttractions(attractions) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
