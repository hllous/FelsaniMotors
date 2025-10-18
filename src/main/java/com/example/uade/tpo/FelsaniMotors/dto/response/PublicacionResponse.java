package com.example.uade.tpo.FelsaniMotors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponse {
    // Datos básicos de la publicación
    private Long idPublicacion;
    
    // IDs para referencias a otras entidades
    private Long idUsuario;
    private Long idAuto;
    
    // Datos básicos de la publicación
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private float precio;
    private Integer descuentoPorcentaje;
    private Date fechaPublicacion;
    private char estado;
    private String metodoDePago;
    
    // Datos adicionales para mostrar en la UI
    private String nombreUsuario;
    private String marcaAuto;
    private String modeloAuto;
    private String imagenPrincipal;
    private Long idFotoPrincipal;
}
