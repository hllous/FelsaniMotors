package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Data
@Table(name = "fotos")
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 

    @ManyToOne(optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    @JsonIgnore                     
    private Publicacion publicacion;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;        

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    private Integer orden = 0;
}

