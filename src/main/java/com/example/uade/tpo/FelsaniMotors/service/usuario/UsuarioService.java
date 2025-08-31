package com.example.uade.tpo.FelsaniMotors.service.usuario;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.entity.Usuario;                      
import com.example.uade.tpo.FelsaniMotors.exceptions.UsuarioDuplicateException; 

/*
GET:
getUsuarios: Devuelve los usuarios en formato paginado, devuelve un 200 OK si existe al menos uno.
getUsuarioById: Devuelve el usuario por ID si existe, si no el controller devuelve un 204 No Content.

POST:
createUsuario: Crea el usuario, si el email ya existe, tira UsuarioDuplicateException, si sale bien el controller devuelve 201 Created. 

*/

public interface UsuarioService {
    Page<Usuario> getUsuarios(Pageable pageable);
    Optional<Usuario> getUsuarioById(Long id);
    Usuario createUsuario(Usuario usuario) throws UsuarioDuplicateException;
}
