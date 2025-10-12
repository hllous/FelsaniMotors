package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transacción no encontrada")
public class TransaccionNoEncontradaException extends RuntimeException {
    
    public TransaccionNoEncontradaException(String message) {
        super(message);
    }
}
