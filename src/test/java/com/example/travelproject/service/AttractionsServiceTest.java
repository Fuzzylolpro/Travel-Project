package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.repository.AttractionRepository;
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
public class AttractionsServiceTest {
    @InjectMocks
    AttractionsService attractionsService;
    @Mock
    AttractionRepository attractionRepository;
    static List<Attractions> attractionsList = null;
    static Attractions attractions = null;
    static Long attractionsId = 10L;

    @BeforeAll
    static void beforeAll() {
        attractionsList = new ArrayList<>();
        attractions = new Attractions();
        attractions.setId(attractionsId);
        attractions.setAttractionsName("Пирамида");
        attractions.setAttractionsInfo("Треугольник");

        attractionsList.add(attractions);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getAllTest() {
        Mockito.when(attractionRepository.findAll()).thenReturn(attractionsList);

        List<Attractions> resultList = attractionsService.getAll();
        Mockito.verify(attractionRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void getAttractionsByIdTest() {
        Mockito.when(attractionRepository.findById(anyLong())).thenReturn(Optional.of(attractions));

        Optional<Attractions> result = attractionsService.getAttractionsId(attractionsId);
        Mockito.verify(attractionRepository, Mockito.times(1)).findById(anyLong());
        Assertions.assertNotNull(result.get());
    }

    @Test
    void createTest() {
        Mockito.when(attractionRepository.save(any())).thenReturn(attractions);

        Boolean result = attractionsService.createAttraction(attractions);
        Mockito.verify(attractionRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }

    @Test
    void updateTest() {
        Mockito.when(attractionRepository.saveAndFlush(any())).thenReturn(attractions);

        Boolean result = attractionsService.updateAttractions(attractions);
        Mockito.verify(attractionRepository, Mockito.times(1)).saveAndFlush(any());
        Assertions.assertTrue(result);
    }

    @Test
    void deleteTest() {
        attractionsService.deleteAttractionsById(10L);
        Mockito.verify(attractionRepository, Mockito.times(1)).deleteById(anyLong());
    }
}
