package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicateException;
import com.example.uade.tpo.FelsaniMotors.service.usuario.UsuarioService;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<Usuario>> getUsuarios(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            return ResponseEntity.ok(
                usuarioService.getUsuarios(PageRequest.of(0, Integer.MAX_VALUE))
            );
        }
        return ResponseEntity.ok(usuarioService.getUsuarios(PageRequest.of(page, size)));
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Usuario> getUsuarioByIdUsuario(@PathVariable Long idUsuario) {
        Optional<Usuario> result = usuarioService.getUsuarioById(idUsuario);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    public ResponseEntity<Object> createUsuario(@RequestBody Usuario usuario)
            throws UsuarioDuplicateException {
        Usuario result = usuarioService.createUsuario(usuario);
        return ResponseEntity.created(URI.create("/usuarios/" + result.getId())).body(result);
    }

    @PostMapping(value = "/{idUsuario}/foto-perfil", consumes = "multipart/form-data")
    public ResponseEntity<Usuario> subirFotoPerfil(
            @PathVariable Long idUsuario,
            @RequestPart("file") MultipartFile file) throws Exception {
        Usuario result = usuarioService.setFotoPerfil(idUsuario, file);
        return ResponseEntity.ok(result);
    }
}

