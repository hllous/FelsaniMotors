package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Error con la foto")
public class FotoException extends RuntimeException {
    
    public FotoException(String message) {
        super(message);
    }
    
    public FotoException(String message, Throwable cause) {
        super(message, cause);
    }
}
