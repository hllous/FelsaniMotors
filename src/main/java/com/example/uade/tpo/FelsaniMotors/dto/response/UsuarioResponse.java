package com.example.uade.tpo.FelsaniMotors.dto.response;

import com.example.uade.tpo.FelsaniMotors.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private Integer telefono;
    private Role rol;
    private Boolean activo;
    private Integer cantidadPublicaciones;
    private Integer cantidadComentarios;
}
