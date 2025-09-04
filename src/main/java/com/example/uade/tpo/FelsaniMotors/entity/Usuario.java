package com.example.uade.tpo.FelsaniMotors.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(builderMethodName = "usuarioBuilder")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario implements UserDetails{

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
    private Boolean activo;
    
    // Relacion con las publicaciones del usuario
    @OneToMany(mappedBy = "usuario")
    private List<Publicacion> publicaciones = new ArrayList<>();
    
    // Relacion con los comentarios del usuario
    @OneToMany(mappedBy = "usuario")
    private List<Comentario> comentarios = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // Las cuentas no expiran
    }

    @Override
    public boolean isAccountNonLocked() {
        return activo; // La cuenta está desbloqueada si está activa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Las credenciales no expiran
    }

    @Override
    public boolean isEnabled() {
        return activo; // El usuario está habilitado si está activo
    }

    public static class UsuarioBuilder {
        public UsuarioBuilder activo(Boolean activo) {
            this.activo = (activo != null) ? activo : true;
            return this;
        }
        public Usuario build() {
            if (this.activo == null) {
                this.activo = true;
            }
            return new Usuario(idUsuario, email, contrasena, nombre, apellido, telefono, rol, activo, publicaciones, comentarios);
        }
    }
}

