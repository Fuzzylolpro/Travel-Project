package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.repository.AttractionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AttractionsService {
    private final AttractionRepository attractionRepository;

    public AttractionsService(AttractionRepository attractionRepository) {
        this.attractionRepository = attractionRepository;
    }

    public List<Attractions> getAll() {
        return attractionRepository.findAll();
    }
}
