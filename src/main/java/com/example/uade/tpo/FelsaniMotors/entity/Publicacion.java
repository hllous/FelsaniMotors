package com.example.uade.tpo.FelsaniMotors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "publicaciones")
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPublicacion;
    
    // Relación con Usuario - versión limpia
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    // Relación con Auto - versión limpia
    @ManyToOne
    @JoinColumn(name = "id_auto", nullable = false)
    private Auto auto;
    
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
    
    // Relación con las fotos
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();
    
    // Métodos helper para manejar la relación bidireccional con Foto
    public void addFoto(Foto foto) {
        fotos.add(foto);
        foto.setPublicacion(this);
    }
    
    public void removeFoto(Foto foto) {
        fotos.remove(foto);
        foto.setPublicacion(null);
    }

}