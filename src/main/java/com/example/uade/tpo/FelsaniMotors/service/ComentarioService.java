package com.example.uade.tpo.FelsaniMotors.service;

import java.util.List;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;

public interface ComentarioService {

    Comentario crear(Comentario comentario);

    Comentario responder(Long idComentarioPadre, Comentario respuesta);

    Comentario obtenerPorId(Long id);

    List<Comentario> listarRaiz(); // sin padre

    List<Comentario> listarRespuestas(Long idComentarioPadre);

    Comentario actualizarTexto(Long idComentario, String nuevoTexto);

    void eliminar(Long idComentario);
}
