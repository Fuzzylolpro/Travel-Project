package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.City;
import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import com.example.travelproject.service.CityService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
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
@WebMvcTest(value = CityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JwtAuthenticationFilter jaf;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CityService cityService;
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static List<City> cityList = null;
    static City city = null;
    @BeforeAll
    static void beforeAll() {
        cityList = new ArrayList<>();
        city = new City();
        city.setId((long) 10);
        city.setName("гиза");
        cityList.add(city);
    }

    @Test
    void getAll() throws Exception {
        when(cityService.getAll()).thenReturn(cityList);
        mockMvc.perform(get("/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(10)));
    }

    @Test
    public void testGetAttractionsId() throws Exception {
        Long id = 1L;

        City city = new City();
        city.setId(id);
        city.setName("Гиза");

        given(cityService.getCityId(any(Long.class))).willReturn(Optional.of(city));

        mockMvc.perform(get("/city/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is(city.getName())));
    }
    @Test
    public void testCreateCity_Success() throws Exception {
        Long id = 1L;
        City city = new City();
        city.setId(id);
        city.setName("Гиза");

        when(cityService.createCity(any(City.class))).thenReturn(true);

        mockMvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(city)))
                .andExpect(status().isCreated());
    }
    @Test
    public void testCreateCity_Conflict() throws Exception {
        Long id = 1L;
        City city = new City();
        city.setId(id);
        city.setName("Гиза");

        when(cityService.createCity(any(City.class))).thenReturn(false);

        mockMvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(city)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testDeleteCity_Success() throws Exception {
        Long cityId = 1L;

        mockMvc.perform(delete("/city/{id}", cityId))
                .andExpect(status().isNoContent());

        verify(cityService, times(1)).deleteCityById(cityId);
    }
    @Test
    public void testUpdateCity_Conflict() throws Exception {
        Long id = 1L;
        City city = new City();
        city.setId(id);
        city.setName("Гиза");

        when(cityService.updateCity(any(City.class))).thenReturn(false);

        mockMvc.perform(put("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(city)))
                .andExpect(status().isConflict());
    }
    @Test
    public void testUpdateCity_Success() throws Exception {
        Long id = 1L;
        City city = new City();
        city.setId(id);
        city.setName("Гиза");

        when(cityService.updateCity(any(City.class))).thenReturn(true);

        mockMvc.perform(put("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(city)))
                .andExpect(status().isNoContent());
    }
}
