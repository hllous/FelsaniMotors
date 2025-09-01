package com.example.uade.tpo.FelsaniMotors.service.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.dto.request.CambioContrasenaRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.UsuarioCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.UsuarioUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.UsuarioResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicadoException;

public interface UsuarioService {
    // Metodos GET
    Page<UsuarioResponse> getUsuarios(Pageable pageable);
    Optional<UsuarioResponse> getUsuarioById(Long idUsuario);
    
    // Metodos POST
    UsuarioResponse createUsuario(UsuarioCreateRequest request) throws UsuarioDuplicadoException;
    
    // Metodos PUT
    UsuarioResponse updateUsuario(Long idUsuario, UsuarioUpdateRequest request);
    UsuarioResponse cambiarContrasena(Long idUsuario, CambioContrasenaRequest request);
    
    // Metodos DELETE
    void deleteUsuario(Long idUsuario);
    
    // Metodos de conversión
    UsuarioResponse convertToDto(Usuario usuario);
}
