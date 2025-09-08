package com.example.uade.tpo.FelsaniMotors.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.dto.request.CambioContrasenaRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.UsuarioCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.UsuarioUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.UsuarioResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.Role;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.ContrasenaIncorrectaException;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicadoException;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioNoEncontradoException;
import com.example.uade.tpo.FelsaniMotors.repository.PublicacionRepository;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PublicacionRepository publicacionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<UsuarioResponse> getUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public Optional<UsuarioResponse> getUsuarioById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(this::convertToDto);
    }

    @Override
    public UsuarioResponse createUsuario(UsuarioCreateRequest request) throws UsuarioDuplicadoException {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new UsuarioDuplicadoException();
        }
        
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setContrasena(request.getContrasena());
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setTelefono(request.getTelefono());
        
        // Si no se especifica ningun rol, se settea el rol USER
        if (request.getRol() == Role.ADMIN) {
            usuario.setRol(Role.ADMIN);
        } else {
            usuario.setRol(Role.USER);
        }
        
        return convertToDto(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse updateUsuario(Long idUsuario, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + idUsuario));
        
        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }
        
        if (request.getApellido() != null) {
            usuario.setApellido(request.getApellido());
        }
        
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        
        return convertToDto(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse cambiarContrasena(Long idUsuario, CambioContrasenaRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + idUsuario));
        
        // Usar passwordEncoder para comparar la contraseña actual
        if (passwordEncoder.matches(request.getContrasenaActual(), usuario.getContrasena())) {
            // Encriptar la nueva contraseña antes de guardarla
            usuario.setContrasena(passwordEncoder.encode(request.getNuevaContrasena()));
        } else {
            throw new ContrasenaIncorrectaException("Contraseña actual incorrecta");
        }
        
        return convertToDto(usuarioRepository.save(usuario));
    }

    @Override
    public void deleteUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + idUsuario));
        
        // Marco el usuario como inactivo
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        // Cambio el estado de todas las publicaciones del usuario
        for (Publicacion publicacion : usuario.getPublicaciones()) {
            publicacion.setEstado('I'); // Inactiva
            publicacionRepository.save(publicacion);
        }
    }
    
    @Override
    public UsuarioResponse convertToDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        UsuarioResponse dto = new UsuarioResponse();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        dto.setActivo(usuario.getActivo());
        dto.setCantidadPublicaciones(usuario.getPublicaciones() != null ? usuario.getPublicaciones().size() : 0);
        dto.setCantidadComentarios(usuario.getComentarios() != null ? usuario.getComentarios().size() : 0);
        
        return dto;
    }
}
