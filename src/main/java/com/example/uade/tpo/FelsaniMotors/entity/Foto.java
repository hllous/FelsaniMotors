package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Blob image;

    @Builder.Default
    private Boolean esPrincipal = false;
    
    @Builder.Default
    private Integer orden = 0;
}
