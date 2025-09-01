package com.example.uade.tpo.FelsaniMotors.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;               

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contrasena;     

    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(nullable = false)
    private Integer telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Role rol = Role.USER; // Por defecto, todos los usuarios son USER
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    // Relación con las publicaciones del usuario
    @OneToMany(mappedBy = "usuario")
    private List<Publicacion> publicaciones = new ArrayList<>();
    
    // Relacion con los comentarios del usuario
    @OneToMany(mappedBy = "usuario")
    private List<Comentario> comentarios = new ArrayList<>();
}

