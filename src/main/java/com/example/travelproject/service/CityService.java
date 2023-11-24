package com.example.travelproject.service;

import com.example.travelproject.domain.City;
import com.example.travelproject.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    public List<City> getAll() {
        return cityRepository.findAll();
    }

    public Optional<City> getCityId(long id) {
        return cityRepository.findById(id);
    }

    public Boolean createCity(City city) {
        try {
            cityRepository.save(city);
            log.info(String.format("city created " + city.getName()));
        } catch (Exception e) {
            log.warn(String.format("error", city.getName()));
            return false;
        }
        return true;
    }

    public void deleteCityById(Long id) {
        cityRepository.deleteById(id);
    }

    public Boolean updateCity(City city) {
        try {
            cityRepository.saveAndFlush(city);
            log.info(String.format("city update id: " + city.getId()));
        } catch (Exception e) {
            log.warn(String.format("error", city.getId(), e));
            return false;
        }
        return true;
    }
}
