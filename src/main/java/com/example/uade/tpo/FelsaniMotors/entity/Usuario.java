package com.example.uade.tpo.FelsaniMotors.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;               

    @Column(nullable = false, unique = true)
    private String email;

    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "contrasena_hash", nullable = false)
    private String contrasena;     

    private String nombre;
    private String apellido;

    
    private Integer telefono;

    @Column(name = "tipo_usuario")
    private String tipoUsuario;    

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_registro")
    private Date fechaRegistro = new Date();
    
    // Relación con las publicaciones del usuario
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Publicacion> publicaciones = new ArrayList<>();
    
    // Relacion con los comentarios del usuario
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Comentario> comentarios = new ArrayList<>();
}
