package com.example.travelproject.controller;

import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.UsersRepository;
import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import com.example.travelproject.security.service.SecurityService;
import com.example.travelproject.service.UsersService;
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
import java.util.HashSet;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UsersController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JwtAuthenticationFilter jaf;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UsersService usersService;
    @MockBean
    UsersRepository usersRepository;
    @MockBean
    SecurityService securityService;
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static List<Users> usersList = null;
    static Users users = null;
    @BeforeAll
    static void beforeAll() {
        usersList = new ArrayList<>();
        users = new Users();
        users.setId((long) 10);
        users.setFirstName("Гена");
        usersList.add(users);
    }
    @Test
    void getAll() throws Exception {
        when(usersService.getAll()).thenReturn(usersList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(10)));
    }
    @Test
    public void testGetUsersId() throws Exception {
        Long id = 1L;

        Users users = new Users();
        users.setId(id);
        users.setFirstName("Гена");

        given(usersService.getUsersId(any(Long.class))).willReturn(Optional.of(users));

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())));
    }
    @Test
    public void testCreateUsers_Success() throws Exception {
        Long id = 1L;

        Users users = new Users();
        users.setId(id);
        users.setFirstName("Гена");

        when(usersService.createUsers(any(Users.class))).thenReturn(true);

        mockMvc.perform(post("/users", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(users)))
                .andExpect(status().isCreated());
    }
    @Test
    public void testCreateUsers_Conflict() throws Exception {
        Long id = 1L;

        Users users = new Users();
        users.setId(id);
        users.setFirstName("Гена");

        when(usersService.createUsers(any(Users.class))).thenReturn(false);

        mockMvc.perform(post("/users", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(users)))
                .andExpect(status().isConflict());
    }
    @Test
    public void testDeleteUsers_Success() throws Exception {
        Long usersId = 1L;

        mockMvc.perform(delete("/users/{id}", usersId))
                .andExpect(status().isNoContent());

        verify(usersService, times(1)).deleteUsersById(usersId);
    }
    @Test
    public void testUpdateUsers_Conflict() throws Exception {
        Long id = 1L;

        Users users = new Users();
        users.setId(id);
        users.setFirstName("Гена");

        when(usersService.updateUsers(any(Users.class))).thenReturn(false);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(users)))
                .andExpect(status().isConflict());
    }
    @Test
    public void testUpdateUsers_Success() throws Exception {
        Long id = 1L;

        Users users = new Users();
        users.setId(id);
        users.setFirstName("Гена");

        when(usersService.updateUsers(any(Users.class))).thenReturn(true);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(users)))
                .andExpect(status().isNoContent());
    }
    @Test
    public void testAddFavoriteAttractions() throws Exception {
        Long userId = 1L;
        Long attractionsId = 2L;

        when(usersService.addFavoriteAttractions(userId, attractionsId)).thenReturn(true);

        mockMvc.perform(post("/users/addFavoriteAttractions")
                        .param("userId", userId.toString())
                        .param("attractionsId", attractionsId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Attractions added to favorites"));
    }
    @Test
    public void testAddFavoriteAttractionsError() throws Exception {
        Long userId = 1L;
        Long attractionsId = 2L;

        when(usersService.addFavoriteAttractions(userId, attractionsId)).thenReturn(false);

        mockMvc.perform(post("/users/addFavoriteAttractions")
                        .param("userId", userId.toString())
                        .param("attractionsId", attractionsId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error adding attractions to favorites"));
    }
    @Test
    public void testDeleteFavoriteAttractions() throws Exception {
        Long userId = 1L;
        Long attractionsId = 2L;

        when(usersService.deleteFavoriteAttractions(userId, attractionsId)).thenReturn(true);

        mockMvc.perform(delete("/users/deleteFavoriteAttractions")
                        .param("userId", userId.toString())
                        .param("attractionsId", attractionsId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Attractions deleted to favorites"));
    }
    @Test
    public void testGetAllFavoriteAttractionsByUser() throws Exception {
        Long userId = 1L;

        Users users = new Users();
        users.setId(userId);
        users.setFavoriteAttractions(new HashSet<>());

        when(usersRepository.findById(userId)).thenReturn(Optional.of(users));
        when(securityService.checkAccessById(userId)).thenReturn(true);

        mockMvc.perform(get("/users/{userId}/favoriteAttractions", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Проверить, что возвращается пустой список любимых достопримечательностей
    }
}
