package com.example.travelproject.repository;

import com.example.travelproject.domain.City;
import com.example.travelproject.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {
}
