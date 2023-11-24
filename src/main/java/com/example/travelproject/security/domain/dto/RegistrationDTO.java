package com.example.travelproject.security.domain.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String firstName;
    private String secondName;
    private Integer age;
    private boolean isMarried;
    private String usersLogin;
    private String usersPassword;
}
