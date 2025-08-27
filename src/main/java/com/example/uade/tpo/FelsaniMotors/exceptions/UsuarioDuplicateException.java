package com.example.uade.tpo.FelsaniMotors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El email ya existe")
public class UsuarioDuplicateException extends Exception { }

