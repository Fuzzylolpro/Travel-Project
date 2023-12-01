package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import com.example.travelproject.service.AttractionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AttractionsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AttractionsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JwtAuthenticationFilter jaf;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AttractionsService attractionsService;
    static List<Attractions> attractionsList = null;
    static Attractions attractions = null;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        attractionsList = new ArrayList<>();
        attractions = new Attractions();
        attractions.setId((long) 10);
        attractions.setAttractionsName("Пирамида");
        attractionsList.add(attractions);
    }

    @Test
    void getAll() throws Exception {
        when(attractionsService.getAll()).thenReturn(attractionsList);
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(10)));
    }

    @Test
    public void testGetAttractionsId() throws Exception {
        Long id = 1L;

        Attractions attraction = new Attractions();
        attraction.setId(id);
        attraction.setAttractionsName("Example Attraction");

        given(attractionsService.getAttractionsId(any(Long.class))).willReturn(Optional.of(attraction));

        mockMvc.perform(get("/attractions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.attractionsName", is(attraction.getAttractionsName())));
    }

    @Test
    public void testCreateAttraction_Success() throws Exception {
        Attractions attraction = new Attractions();
        attraction.setId(1L);
        attraction.setAttractionsName("Example Attraction");

        when(attractionsService.createAttraction(any(Attractions.class))).thenReturn(true);

        mockMvc.perform(post("/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(attraction)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateAttraction_Conflict() throws Exception {
        Attractions attraction = new Attractions();
        attraction.setId(1L);
        attraction.setAttractionsName("Example Attraction");

        when(attractionsService.createAttraction(any(Attractions.class))).thenReturn(false);

        mockMvc.perform(post("/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(attraction)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testDeleteAttraction_Success() throws Exception {
        Long attractionId = 1L;

        mockMvc.perform(delete("/attractions/{id}", attractionId))
                .andExpect(status().isNoContent());

        verify(attractionsService, times(1)).deleteAttractionsById(attractionId);
    }

    @Test
    public void testUpdateAttraction_Conflict() throws Exception {
        Attractions attraction = new Attractions();
        attraction.setId(1L);
        attraction.setAttractionsName("Example Attraction");

        when(attractionsService.updateAttractions(any(Attractions.class))).thenReturn(false);

        mockMvc.perform(put("/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(attraction)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateAttraction_Success() throws Exception {
        Attractions attraction = new Attractions();
        attraction.setId(1L);
        attraction.setAttractionsName("Example Attraction");

        when(attractionsService.updateAttractions(any(Attractions.class))).thenReturn(true);

        mockMvc.perform(put("/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(attraction)))
                .andExpect(status().isNoContent());
    }
}


