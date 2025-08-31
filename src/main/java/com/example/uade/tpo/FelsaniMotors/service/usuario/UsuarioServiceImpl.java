package com.example.uade.tpo.FelsaniMotors.service.usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicateException;
import com.example.uade.tpo.FelsaniMotors.repository.UsuarioRepository;

/*
GET:
getUsuarios: Devuelve los usuarios paginados desde el repositorio, devuelve un 204 si no hay usuarios.
getUsuarioById: Devuelve el usuario por ID si existe, si no devuelve null.

POST:
createUsuario: Crea el usuario y lo guarda, si el email ya existe, tira UsuarioDuplicateException; cuando se crea bien, el controller devuelve 201 Created.
*/

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarios;

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
}
