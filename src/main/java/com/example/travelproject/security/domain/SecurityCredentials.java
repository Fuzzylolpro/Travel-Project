package com.example.travelproject.security.domain;

import com.example.travelproject.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity(name = "security_credentials")
@Data
@Component
public class SecurityCredentials {
    @Id
    @SequenceGenerator(name = "security_users", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "security_users", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "users_login")
    private String usersLogin;

    @Column(name = "users_password")
    private String usersPassword;

    @Column(name = "users_role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "users_id")
    private Long usersId;
}
