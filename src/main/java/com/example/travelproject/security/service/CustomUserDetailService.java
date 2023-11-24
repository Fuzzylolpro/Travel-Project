package com.example.travelproject.security.service;

import com.example.travelproject.exception_resolver.UserFromDatabaseNotFound;
import com.example.travelproject.security.domain.SecurityCredentials;
import com.example.travelproject.security.repository.SecurityCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    final private SecurityCredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //логин -> логин пароль роль
        //проверка
        // создаем userdetail
        Optional<SecurityCredentials> userFromDatabase = credentialsRepository.getByUsersLogin(username);
        if (userFromDatabase.isEmpty()) {
            throw new UserFromDatabaseNotFound();
        }
        SecurityCredentials user = userFromDatabase.get();
        return User
                .withUsername(user.getUsersLogin())
                .password(user.getUsersPassword())
                .roles(user.getRole().toString())
                .build();

    }
}
