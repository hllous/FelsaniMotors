package com.example.uade.tpo.FelsaniMotors.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComentario;

    // N:1 con Publicacion
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;

    // Autorreferencia: comentario padre (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comentario_padre")
    private Comentario padre;

    // Hijos/inverso (opcional activar)
    @OneToMany(mappedBy = "padre", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> respuestas;

    @Column(nullable = false, length = 2000)
    private String texto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
}
