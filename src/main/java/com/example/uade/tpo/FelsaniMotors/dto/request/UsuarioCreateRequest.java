package com.example.uade.tpo.FelsaniMotors.dto.request;

import com.example.uade.tpo.FelsaniMotors.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateRequest {
    private String email;
    private String contrasena;
    private String nombre;
    private String apellido;
    private Integer telefono;
    private Role rol;
}
