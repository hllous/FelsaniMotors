package com.example.uade.tpo.FelsaniMotors.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionCreateRequest {
    // IDs para referencias
    private Long idUsuario;
    private Long idAuto;
    
    // Datos básicos de la publicación
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private float precio;
    private String metodoDePago;
}
