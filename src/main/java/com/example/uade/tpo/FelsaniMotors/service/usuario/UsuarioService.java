package com.example.uade.tpo.FelsaniMotors.service.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.entity.Usuario;                      
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicateException; 

public interface UsuarioService {
    Page<Usuario> getUsuarios(Pageable pageable);
    Optional<Usuario> getUsuarioById(Long id);
    Usuario createUsuario(Usuario usuario) throws UsuarioDuplicateException;
}
