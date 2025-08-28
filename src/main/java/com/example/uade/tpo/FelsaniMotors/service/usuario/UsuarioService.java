package com.example.uade.tpo.FelsaniMotors.service.usuario;

import java.io.IOException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Usuario;                      
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicateException; 

public interface UsuarioService {
    Page<Usuario> getUsuarios(Pageable pageable);
    Optional<Usuario> getUsuarioById(Long id);
    Usuario createUsuario(Usuario usuario) throws UsuarioDuplicateException;
    Usuario setFotoPerfil(Long idUsuario, MultipartFile file) throws IOException;
}
