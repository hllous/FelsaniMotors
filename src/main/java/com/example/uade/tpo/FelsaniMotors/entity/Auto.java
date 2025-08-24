package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table (name = "auto")
public class Auto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAuto;

    @Column
    private String marca;

    @Column
    private String modelo;

    @Column
    private Integer anio;

    @Column
    private String estado;

    @Column
    private Float kilometraje;

    @Column
    private String combustible;

    @Column
    private Integer capacidadTanque;

    @Column
    private String tipoCaja;

    @Column
    private String motor;

    @Column
    private String Categoria;

    /* hay que sacar el comentario, y modificar si hay otro nombre de la clase Publicacion
    @OneToMany(mappedBy = "auto")
    private List<Publicacion> publicaciones;
    */
}