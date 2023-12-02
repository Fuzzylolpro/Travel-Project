package com.example.travelproject.service;

import com.example.travelproject.domain.City;
import com.example.travelproject.repository.CityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {
    @InjectMocks
    CityService cityService;
    @Mock
    CityRepository cityRepository;
    static List<City> cityList = null;
    static City city = null;
    static Long cityId = 10L;

    @BeforeAll
    static void beforeAll() {
        cityList = new ArrayList<>();
        city = new City();
        city.setId(cityId);
        city.setName("Гиза");

        cityList.add(city);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getAllTest() {
        Mockito.when(cityRepository.findAll()).thenReturn(cityList);

        List<City> resultList = cityService.getAll();
        Mockito.verify(cityRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void getCityByIdTest() {
        Mockito.when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));

        Optional<City> result = cityService.getCityId(cityId);
        Mockito.verify(cityRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.get());
    }

    @Test
    void createTest() {
        Mockito.when(cityRepository.save(any())).thenReturn(city);

        Boolean result = cityService.createCity(city);
        Mockito.verify(cityRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }

    @Test
    void updateTest() {
        Mockito.when(cityRepository.saveAndFlush(any())).thenReturn(city);

        Boolean result = cityService.updateCity(city);
        Mockito.verify(cityRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
    }

    @Test
    void deleteTest() {
        cityService.deleteCityById(10L);
        Mockito.verify(cityRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
