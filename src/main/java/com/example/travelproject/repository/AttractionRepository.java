package com.example.travelproject.repository;

import com.example.travelproject.domain.Attractions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionRepository extends JpaRepository<Attractions, Long> {
}
