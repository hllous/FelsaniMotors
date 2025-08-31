package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data

// Settea el nombre de la tabla en la db
@Table(name = "categorias")

// Crea constructores con y sin los parametros
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;
    
    @Column
    private String categoria;

    @Column
    private String marca;
    
    @Column
    private String imagen;
}
