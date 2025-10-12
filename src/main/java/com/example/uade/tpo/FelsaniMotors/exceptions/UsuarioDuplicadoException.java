package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Usuario duplicado")
public class UsuarioDuplicadoException extends RuntimeException {
    public UsuarioDuplicadoException() {
        super("El email ya existe");
    }
    
    public UsuarioDuplicadoException(String mensaje) {
        super(mensaje);
    }
}

