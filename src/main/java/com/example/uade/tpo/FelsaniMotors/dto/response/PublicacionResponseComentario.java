package com.example.uade.tpo.FelsaniMotors.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionResponseComentario {
    private Long idPublicacion;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private float precio;
    private Date fechaPublicacion;
    private char estado;
}
