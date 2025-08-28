package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data

@Table(name = "publicaciones")

@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPublicacion;

    @Column(nullable = false)
    private Long idUsuario;
    
    @Column(nullable = false)
    private Long idAuto;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column
    private String descripcion;
    
    @Column(length = 150)
    private String ubicacion;
    
    @Column(nullable = false)
    private float precio;
    
    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;
    
    @Column(nullable = false)
    private char estado;
    
    @Column(name = "metodo_pago", length = 50)
    private String metodoDePago;
}