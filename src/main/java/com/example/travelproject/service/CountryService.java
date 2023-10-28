package com.example.travelproject.service;

import com.example.travelproject.domain.Country;
import com.example.travelproject.repository.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    public Optional<Country> getCountryId(long id) {
        return countryRepository.findById(id);
    }

    public Boolean createCountry(Country country) {
        try {
            countryRepository.save(country);
            log.info(String.format("country created " + country.getCountryName()));
        } catch (Exception e) {
            log.warn(String.format("error", country.getCountryName()));
            return false;
        }
        return true;
    }

    public void deleteCountryById(Long id) {
        countryRepository.deleteById(id);
    }

    public Boolean updateCountry(Country country) {
        try {
            countryRepository.saveAndFlush(country);
            log.info(String.format("country update id: " + country.getId()));
        } catch (Exception e) {
            log.warn(String.format("error", country.getId(), e));
            return false;
        }
        return true;
    }
}

