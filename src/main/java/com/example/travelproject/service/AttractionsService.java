package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.repository.AttractionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Optional<Attractions> getAttractionsId(long id) {
        return attractionRepository.findById(id);
    }
    public Boolean createAttraction(Attractions attractions) {
        try {
            attractionRepository.save(attractions);
            log.info(String.format("attraction created"+attractions.getAttractionsName()));
        }catch (Exception e){
            log.warn(String.format("error",attractions.getAttractionsName()));
            return false;
        }
        return true;
    }
}
