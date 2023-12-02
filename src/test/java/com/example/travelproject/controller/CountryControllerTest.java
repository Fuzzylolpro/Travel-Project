package com.example.travelproject.controller;

import com.example.travelproject.domain.Country;
import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import com.example.travelproject.service.CountryService;
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

import static org.hamcrest.Matchers.hasSize;
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
@WebMvcTest(value = CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CountryControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JwtAuthenticationFilter jaf;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CountryService countryService;
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static List<Country> countryList = null;
    static Country country = null;
    @BeforeAll
    static void beforeAll() {
        countryList = new ArrayList<>();
        country = new Country();
        country.setId((long) 10);
        country.setCountryName("Польша");
        countryList.add(country);
    }
    @Test
    void getAll() throws Exception {
        when(countryService.getAll()).thenReturn(countryList);
        mockMvc.perform(get("/country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(10)));
    }
    @Test
    public void testGetCountryId() throws Exception {
        Long id = 1L;

        Country country = new Country();
        country.setId(id);
        country.setCountryName("Poland");

        given(countryService.getCountryId(any(Long.class))).willReturn(Optional.of(country));

        mockMvc.perform(get("/country/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.countryName", is(country.getCountryName())));
    }
    @Test
    public void testCreateCity_Success() throws Exception {
        Long id = 1L;

        Country country = new Country();
        country.setId(id);
        country.setCountryName("Poland");

        when(countryService.createCountry(any(Country.class))).thenReturn(true);

        mockMvc.perform(post("/country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(country)))
                .andExpect(status().isCreated());
    }
    @Test
    public void testCreateCity_Conflict() throws Exception {
        Long id = 1L;

        Country country = new Country();
        country.setId(id);
        country.setCountryName("Poland");

        when(countryService.createCountry(any(Country.class))).thenReturn(false);

        mockMvc.perform(post("/country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(country)))
                .andExpect(status().isConflict());
    }
    @Test
    public void testDeleteCountry_Success() throws Exception {
        Long countryId = 1L;

        mockMvc.perform(delete("/country/{id}", countryId))
                .andExpect(status().isNoContent());

        verify(countryService, times(1)).deleteCountryById(countryId);
    }
    @Test
    public void testUpdateCountry_Conflict() throws Exception {
        Long id = 1L;

        Country country = new Country();
        country.setId(id);
        country.setCountryName("Poland");

        when(countryService.updateCountry(any(Country.class))).thenReturn(false);

        mockMvc.perform(put("/country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(country)))
                .andExpect(status().isConflict());
    }
    @Test
    public void testUpdateCountry_Success() throws Exception {
        Long id = 1L;

        Country country = new Country();
        country.setId(id);
        country.setCountryName("Poland");

        when(countryService.updateCountry(any(Country.class))).thenReturn(true);

        mockMvc.perform(put("/country")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(country)))
                .andExpect(status().isNoContent());
    }

}
