package com.example.uade.tpo.FelsaniMotors.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponse {
    // Datos básicos de la publicación
    private Long idPublicacion;
    
    // IDs para referencias a otras entidades
    private Long idUsuario;
    private Long idAuto;
    
    // Datos básicos de la publicación y auto
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private float precio;
    private Date fechaPublicacion;
    private char estado;
    private String metodoDePago;
    private String marcaAuto;
    private String modeloAuto;
    private Integer anio;
    private String estadoAuto;
    private Float kilometraje;
    private String combustible;
    private String tipoCategoria;
    private Integer capacidadTanque;
    private String tipoCaja;
    private String motor;
    
    // Datos adicionales para mostrar en la UI
    private String nombreUsuario;
    private String imagenPrincipal;
    private Long idFotoPrincipal;
}