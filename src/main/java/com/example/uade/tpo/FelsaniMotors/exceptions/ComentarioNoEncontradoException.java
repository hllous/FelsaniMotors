package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Comentario no encontrado")
public class ComentarioNoEncontradoException extends RuntimeException {

    public ComentarioNoEncontradoException(Long id) {
        super("No existe el comentario con id=" + id);
    }
}
