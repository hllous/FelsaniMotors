package com.example.uade.tpo.FelsaniMotors.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDTO {
    private Long id;
    private Long idUsuario;
    private Long idAuto;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private float precio;
    private Date fechaPublicacion;
    private char estado;
    private String metodoDePago;
    
    // Artributos adicionales para mostrar
    private String nombreUsuario;
    private String marcaAuto;
    private String modeloAuto;
    private String imagenPrincipal;

}