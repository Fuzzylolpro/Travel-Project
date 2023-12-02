package com.example.travelproject.service;

import com.example.travelproject.domain.Users;
import com.example.travelproject.repository.UsersRepository;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {
    @Mock
    private SecurityService securityService;
    @InjectMocks
    UsersService usersService;
    @Mock
    UsersRepository usersRepository;
    static List<Users> usersList = null;
    static Users users = null;
    static Long usersId = 10L;

    @BeforeAll
    static void beforeAll() {
        usersList = new ArrayList<>();
        users = new Users();
        users.setId(usersId);
        users.setFirstName("Gena");

        usersList.add(users);

        Authentication authenticationMock = mock(Authentication.class);
        SecurityContext securityContextMock = mock(SecurityContext.class);
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    void getAllTest() {
        when(usersRepository.findAll()).thenReturn(usersList);

        List<Users> resultList = usersService.getAll();
        verify(usersRepository, Mockito.times(1)).findAll();
        Assertions.assertNotNull(resultList);
    }

    @Test
    void getUsersByIdTest() {
        when(securityService.checkAccessById(usersId)).thenReturn(true);
        when(usersRepository.findById(usersId)).thenReturn(Optional.of(users));
        Optional<Users> result = usersService.getUsersId(usersId);
        verify(securityService).checkAccessById(usersId);
        verify(usersRepository).findById(usersId);
        assertTrue(result.isPresent());
    }

    @Test
    void createTest() {
        Mockito.when(usersRepository.save(any())).thenReturn(users);

        Boolean result = usersService.createUsers(users);
        Mockito.verify(usersRepository, Mockito.times(1)).save(any());
        Assertions.assertTrue(result);
    }
}
