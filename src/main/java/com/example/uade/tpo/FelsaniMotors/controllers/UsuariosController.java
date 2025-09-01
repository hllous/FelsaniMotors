package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade.tpo.FelsaniMotors.dto.request.CambioContrasenaRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.UsuarioCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.UsuarioUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.UsuarioResponse;
import com.example.uade.tpo.FelsaniMotors.exceptions.ContrasenaIncorrectaException;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicadoException;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioNoEncontradoException;
import com.example.uade.tpo.FelsaniMotors.service.usuario.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    // --- Métodos GET ---
    
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> getUsuarios(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        PageRequest pageRequest;
        if (page == null || size == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageRequest = PageRequest.of(page, size);
        }
        
        Page<UsuarioResponse> usuarios = usuarioService.getUsuarios(pageRequest);
        
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Long idUsuario) {
        Optional<UsuarioResponse> usuarioOpt = usuarioService.getUsuarioById(idUsuario);
        if (usuarioOpt.isPresent()) {
            return ResponseEntity.ok(usuarioOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Métodos POST ---
    
    @PostMapping
    public ResponseEntity<UsuarioResponse> createUsuario(@RequestBody UsuarioCreateRequest request)
            throws UsuarioDuplicadoException {
        
        UsuarioResponse resultado = usuarioService.createUsuario(request);
        
        return ResponseEntity
                .created(URI.create("/api/usuarios/" + resultado.getIdUsuario()))
                .body(resultado);
    }

    // --- Métodos PUT ---
    
    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @PathVariable Long idUsuario,
            @RequestBody UsuarioUpdateRequest request) {
        
        try {
            UsuarioResponse resultado = usuarioService.updateUsuario(idUsuario, request);
            return ResponseEntity.ok(resultado);
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{idUsuario}/cambiar-contrasena")
    public ResponseEntity<UsuarioResponse> cambiarContrasena(
            @PathVariable Long idUsuario,
            @RequestBody CambioContrasenaRequest request) {
        
        try {
            UsuarioResponse resultado = usuarioService.cambiarContrasena(idUsuario, request);
            return ResponseEntity.ok(resultado);
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (ContrasenaIncorrectaException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- Métodos DELETE ---
    
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long idUsuario) {
        try {
            usuarioService.deleteUsuario(idUsuario);
            return ResponseEntity.noContent().build();
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

