package com.example.travelproject.security.repository;

import com.example.travelproject.security.domain.SecurityCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityCredentialsRepository extends JpaRepository<SecurityCredentials, Long> {
    Optional<SecurityCredentials> getByUsersLogin(String login);

    @Query(
            nativeQuery = true,
            value = "SELECT users_id FROM security_credentials WHERE users_login = ?1")
    Long findUserIdByLogin(String login);
}
