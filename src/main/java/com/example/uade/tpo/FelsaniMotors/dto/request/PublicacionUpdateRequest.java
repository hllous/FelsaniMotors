package com.example.uade.tpo.FelsaniMotors.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionUpdateRequest {

    private String titulo;
    private String descripcion;
    private String ubicacion;
    private float precio;
    private String metodoDePago;
    private Long idUsuario;
}
