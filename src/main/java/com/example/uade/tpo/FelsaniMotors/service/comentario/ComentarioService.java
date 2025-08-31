package com.example.uade.tpo.FelsaniMotors.service.comentario;

import java.util.List;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;

public interface ComentarioService {

    //Crea un nuevo comentario para una publicación
    Comentario crearComentario(Long idPublicacion, Comentario comentario);

    //Crea una respuesta a un comentario existente
    Comentario crearRespuesta(Long idPublicacion, Long idComentarioPadre, Comentario respuesta);

    //Obtiene un comentario por su ID
    Comentario buscarPorId(Long idComentario);

    //Lista todos los comentarios principales (sin padre) de una publicación
    List<Comentario> listarComentariosPrincipales(Long idPublicacion);

    //Lista todas las respuestas a un comentario
    List<Comentario> listarRespuestas(Long idComentarioPadre);

    //Lista todos los comentarios de una publicación organizados jerárquicamente
    //(comentarios principales seguidos por sus respuestas)
    List<Comentario> listarComentariosOrdenados(Long idPublicacion);

    //Actualiza el texto de un comentario
    Comentario actualizarTexto(Long idComentario, String nuevoTexto);

    //Elimina un comentario y todas sus respuestas
    void eliminarComentario(Long idComentario);
}
