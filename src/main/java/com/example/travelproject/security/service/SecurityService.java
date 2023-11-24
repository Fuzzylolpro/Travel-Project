package com.example.travelproject.security.service;

import com.example.travelproject.domain.Users;
import com.example.travelproject.domain.Role;
import com.example.travelproject.exception_resolver.SameUserInDatabaseException;
import com.example.travelproject.repository.UsersRepository;
import com.example.travelproject.security.domain.SecurityCredentials;
import com.example.travelproject.security.domain.dto.AuthRequest;
import com.example.travelproject.security.domain.dto.RegistrationDTO;
import com.example.travelproject.security.repository.SecurityCredentialsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SecurityService {

    private final SecurityCredentialsRepository securityCredentialsRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public Optional<String> generateToken(AuthRequest authRequest) {
        Optional<SecurityCredentials> personCredentials = securityCredentialsRepository.getByUsersLogin(authRequest.getLogin());
        if (personCredentials.isPresent() && passwordEncoder.matches(authRequest.getPassword(),
                personCredentials.get().getUsersPassword())) {
            return Optional.of(jwtUtils.generateJwtToken(authRequest.getLogin()));
        }
        return Optional.empty();
    }

    @Transactional(rollbackOn = Exception.class)
    public void registration(RegistrationDTO registrationDTO) {
        Optional<SecurityCredentials> result = securityCredentialsRepository.getByUsersLogin(registrationDTO.getUsersLogin());
        if (result.isPresent()) {
            throw new SameUserInDatabaseException();
        }

        Users users = new Users();
        users.setFirstName(registrationDTO.getFirstName());
        users.setSecondName(registrationDTO.getSecondName());
        users.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        users.setAge(registrationDTO.getAge());
        users.setIsMarried(false);
        Users userInfoResult = usersRepository.save(users);

        SecurityCredentials securityCredentials = new SecurityCredentials();
        securityCredentials.setUsersLogin(registrationDTO.getUsersLogin());
        securityCredentials.setUsersPassword(passwordEncoder.encode(registrationDTO.getUsersPassword()));
        securityCredentials.setRole(Role.USER);
        securityCredentials.setUsersId(userInfoResult.getId());
        securityCredentialsRepository.save(securityCredentials);
    }

    public boolean checkAccessById(Long id){
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        String userRole = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get());
        Long userId = securityCredentialsRepository.findUserIdByLogin(userLogin);
        return (userId.equals(id) || userRole.equals("ROLE_ADMIN"));
    }
}