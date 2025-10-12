package com.example.uade.tpo.FelsaniMotors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseComentario {
    private Long idUsuario;
    private String email;
    private String nombre;
    private String apellido;
    private Integer telefono;
    private String rol;
}
