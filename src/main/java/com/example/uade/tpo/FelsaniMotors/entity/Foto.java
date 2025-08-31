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
    
    @Lob
    @JsonIgnore
    private byte[] datos;

    
    private Boolean esPrincipal = false;

    
    private Integer orden = 0;
}
