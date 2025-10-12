package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Auto duplicado")
public class AutoDuplicateException extends RuntimeException {
    public AutoDuplicateException(String message) {
        super(message);
    }
}