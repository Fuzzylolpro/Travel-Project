package com.example.travelproject.service;

import com.example.travelproject.domain.City;
import com.example.travelproject.domain.Country;
import com.example.travelproject.repository.CityRepository;
import com.example.travelproject.repository.CountryRepository;
import com.example.travelproject.security.repository.SecurityCredentialsRepository;
import com.example.travelproject.security.service.SecurityService;
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
public class CountryServiceTest {
    @InjectMocks
    CountryService countryService;
    @Mock
    CountryRepository countryRepository;
    static List<Country> countryList = null;
    static Country country = null;
    static Long countryId = 10L;
    @BeforeAll
    static void beforeAll() {
        countryList = new ArrayList<>();
        country = new Country();
        country.setId(countryId);
        country.setCountryName("Польша");

        countryList.add(country);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }
    @Test
    void getAllTest() {
        Mockito.when(countryRepository.findAll()).thenReturn(countryList);

        List<Country> resultList = countryService.getAll();
        Mockito.verify(countryRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }
    @Test
    void getCountryByIdTest() {
        Mockito.when(countryRepository.findById(anyLong())).thenReturn(Optional.of(country));

        Optional<Country> result = countryService.getCountryId(countryId);
        Mockito.verify(countryRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.get());
    }
    @Test
    void createTest() {
        Mockito.when(countryRepository.save(any())).thenReturn(country);

        Boolean result = countryService.createCountry(country);
        Mockito.verify(countryRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }
    @Test
    void updateTest() {
        Mockito.when(countryRepository.saveAndFlush(any())).thenReturn(country);

        Boolean result = countryService.updateCountry(country);
        Mockito.verify(countryRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
    }
    @Test
    void deleteTest() {
        countryService.deleteCountryById(10L);
        Mockito.verify(countryRepository, Mockito.times(1)).deleteById(anyLong());
    }
}

