package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Contraseña incorrecta")
public class ContrasenaIncorrectaException extends RuntimeException {
    public ContrasenaIncorrectaException(String message) {
        super(message);
    }
}
