package com.example.uade.tpo.FelsaniMotors.controllers.auth;

import com.example.uade.tpo.FelsaniMotors.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private Integer telefono;
}
