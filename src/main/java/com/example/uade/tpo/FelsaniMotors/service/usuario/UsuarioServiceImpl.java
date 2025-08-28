package com.example.uade.tpo.FelsaniMotors.service.usuario;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicateException;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;
import com.example.uade.tpo.FelsaniMotors.service.AlmacenamientoService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarios;

    @Autowired
    private AlmacenamientoService storage;

    @Override
    public Page<Usuario> getUsuarios(Pageable pageable) {
        return usuarios.findAll(pageable);
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarios.findById(id);
    }

    @Override
    public Usuario createUsuario(Usuario usuario) throws UsuarioDuplicateException {
        if (usuarios.existsByEmail(usuario.getEmail())) {
            throw new UsuarioDuplicateException();
        }
        return usuarios.save(usuario);
    }

    @Override
    public Usuario setFotoPerfil(Long idUsuario, MultipartFile file) throws IOException {
        Usuario u = usuarios.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String contentType = file.getContentType();
        if (file.isEmpty() || contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Debe subir una imagen válida");
        }

        String url = storage.guardar(file);   
        u.setFotoPerfil(url);
        return usuarios.save(u);
    }
}
