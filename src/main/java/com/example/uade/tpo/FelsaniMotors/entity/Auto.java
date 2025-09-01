package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table (name = "autos")
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

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToOne(mappedBy = "auto")
    @JsonIgnore
    private Publicacion publicacion;
}