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
    
    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    // Relación con Auto (ahora OneToOne)
    @OneToOne
    @JoinColumn(name = "id_auto", nullable = false)
    private Auto auto;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column
    private String descripcion;
    
    @Column
    private String ubicacion;
    
    @Column(nullable = false)
    private float precio;
    
    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;
    
    @Column(nullable = false)
    private char estado;
    
    @Column(name = "metodo_pago")
    private String metodoDePago;

    // cascade = propaga operaciones de persistencia de entidad padre -> hijo
    // orphan removal = elimina hijos cuando se elimina un padre
    
    // Relación con las fotos
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();
    
    // Relacion con los comentarios
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();
    
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