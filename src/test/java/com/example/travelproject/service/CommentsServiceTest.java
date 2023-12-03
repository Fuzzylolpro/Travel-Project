package com.example.travelproject.service;

import com.example.travelproject.domain.Attractions;
import com.example.travelproject.domain.Comments;
import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.CommentRepository;
import com.example.travelproject.security.service.SecurityService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceTest {
    @InjectMocks
    CommentsService commentsService;

    @Mock
    SecurityService securityService;

    @Mock
    CommentRepository commentRepository;
    static List<Comments> commentsList = null;
    static Comments comments = null;
    static Long commentsId = 10L;

    @BeforeAll
    static void beforeAll() {
        commentsList = new ArrayList<>();
        comments = new Comments();
        comments.setId(commentsId);
        comments.setText("Пирамида");
        commentsList.add(comments);

        Authentication authenticationMock = Mockito.mock(Authentication.class);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getAllTest() {
        Mockito.when(commentRepository.findAll()).thenReturn(commentsList);

        List<Comments> resultList = commentsService.getAll();
        Mockito.verify(commentRepository, Mockito.times(1)).findAll();
        assertNotNull(resultList);
    }

    @Test
    void getCommentsByIdTest() {
        Mockito.when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comments));

        Optional<Comments> result = commentsService.getCommentsId(commentsId);
        Mockito.verify(commentRepository, Mockito.times(1)).findById(anyLong());
        assertNotNull(result.get());
    }

    @Test
    public void testAddComments() {
        String text = "Test comment";
        Users userDTO = new Users();
        userDTO.setId(1L);
        Attractions attractions = new Attractions();
        attractions.setId(1L);
        Mockito.when(securityService.checkAccessById(userDTO.getId())).thenReturn(true);
        Mockito.when(commentRepository.save(Mockito.any(Comments.class))).thenReturn(new Comments());
        Comments result = commentsService.addComments(text, userDTO, attractions);
        Mockito.verify(securityService).checkAccessById(userDTO.getId());
        Mockito.verify(commentRepository).save(Mockito.any(Comments.class));
        assertNotNull(result);
    }

    @Test
    public void testDeleteCommentsById_Success() {
        Long id = 1L;
        Long userId = 2L;
        Users user = new Users();
        user.setId(userId);

        Comments comments = new Comments();
        comments.setId(id);
        comments.setUsers(user);

        Mockito.when(commentRepository.getReferenceById(id)).thenReturn(comments);
        Mockito.when(securityService.checkAccessById(userId)).thenReturn(true);

        boolean result = commentsService.deleteCommentsById(id, userId);

        Mockito.verify(commentRepository, Mockito.times(1)).deleteById(id);
        assertTrue(result);
    }
}
