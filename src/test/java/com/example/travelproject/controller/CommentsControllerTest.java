package com.example.travelproject.controller;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.domain.DTO.UserDTO;
import com.example.travelproject.domain.Users;
import com.example.travelproject.security.filter.JwtAuthenticationFilter;
import com.example.travelproject.service.CommentsService;
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
import static org.mockito.ArgumentMatchers.anyString;
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
@WebMvcTest(value = CommentsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    JwtAuthenticationFilter jaf;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    CommentsService commentsService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static List<Comments> commentsList = null;
    static Comments comments = null;

    @BeforeAll
    static void beforeAll() {
        commentsList = new ArrayList<>();
        comments = new Comments();
        comments.setId((long) 10);
        comments.setText("Клево");
        commentsList.add(comments);
    }

    @Test
    void getAll() throws Exception {
        when(commentsService.getAll()).thenReturn(commentsList);
        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo(10)));
    }

    @Test
    public void testGetCommentsId() throws Exception {
        Long id = 1L;

        Comments comments = new Comments();
        comments.setId(id);
        comments.setText("Клево");

        given(commentsService.getCommentsId(any(Long.class))).willReturn(Optional.of(comments));

        mockMvc.perform(get("/comments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.text", is(comments.getText())));
    }

    @Test
    public void testGetAllById() throws Exception {
        Long userId = 1L;
        List<Comments> comments = new ArrayList<>();
        given(commentsService.getCommentsByUserId(userId)).willReturn(comments);

        mockMvc.perform(get("/comments/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", Matchers.hasSize(comments.size())));
    }

    @Test
    public void testCreateComment() throws Exception {
        String text = "Test comment";
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Long attractionId = 1L;

        Users users = new Users();
        users.setId(userDTO.getId());
        Attractions attraction = new Attractions();
        attraction.setId(attractionId);
        Comments comment = new Comments();
        comment.setText(text);
        comment.setUsers(users);
        comment.setAttractions(attraction);

        when(commentsService.addComments(anyString(), any(Users.class), any(Attractions.class))).thenReturn(comment);

        mockMvc.perform(post("/comments")
                        .param("text", text)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .param("attractionId", String.valueOf(attractionId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text", is(text)))
                .andExpect(jsonPath("$.users.id", is(userDTO.getId().intValue())));


        verify(commentsService).addComments(text, users, attraction);
    }

    @Test
    public void testDeleteComments_Success() throws Exception {
        Long commentsId = 1L;
        Long usersId = 2L;

        when(commentsService.deleteCommentsById(commentsId, usersId)).thenReturn(true);

        mockMvc.perform(delete("/comments/{id}", commentsId)
                        .param("userId", String.valueOf(usersId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));

        verify(commentsService, times(1)).deleteCommentsById(commentsId, usersId);
    }
    @Test
    public void testUpdateComments_Success() throws Exception {
        Comments comments = new Comments(); // Создание тестового объекта Comments

        when(commentsService.updateComments(comments)).thenReturn(true);

        mockMvc.perform(put("/comments")
                        .content(asJsonString(comments))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(commentsService, times(1)).updateComments(comments);
    }
}

