package com.example.uade.tpo.FelsaniMotors.exceptions;

public class ComentarioNoEncontradoException extends RuntimeException {

    public ComentarioNoEncontradoException(Long id) {

        super("No existe el comentario con id=" + id);
    }
}
