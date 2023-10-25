package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.service.AttractionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
