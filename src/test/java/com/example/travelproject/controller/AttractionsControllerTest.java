package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Users;
import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import com.example.travelproject.service.AttractionsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        Mockito.when(attractionsService.getAll()).thenReturn(attractionsList);
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(10)));
    }
}
