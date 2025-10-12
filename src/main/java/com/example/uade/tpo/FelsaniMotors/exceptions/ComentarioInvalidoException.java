package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Comentario inválido")
public class ComentarioInvalidoException extends RuntimeException {
    public ComentarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
