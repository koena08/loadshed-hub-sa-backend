package com.jumpstart.loadshedhub.dto;

import lombok.Data;

//captures incoming JSON payloads from registration & login
@Data
public class AuthRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
}
