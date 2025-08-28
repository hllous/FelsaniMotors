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
    @Column(name = "id_foto")
    private Long idFoto;                 

    @ManyToOne(optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    @JsonIgnore                     
    private Publicacion publicacion;
    
    // Datos binarios de la imagen
    @Lob
    @JsonIgnore
    private byte[] datos;

    // Indica si esta es la foto principal de la publicación
    private Boolean esPrincipal = false;

    // Orden de la foto para mostrar en la UI
    private Integer orden = 0;
}
