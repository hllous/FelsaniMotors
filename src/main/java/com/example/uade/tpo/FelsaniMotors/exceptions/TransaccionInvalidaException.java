package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Transacción inválida")
public class TransaccionInvalidaException extends RuntimeException {
    
    public TransaccionInvalidaException(String message) {
        super(message);
    }
}
