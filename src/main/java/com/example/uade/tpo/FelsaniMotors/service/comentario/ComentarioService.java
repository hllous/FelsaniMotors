package com.example.uade.tpo.FelsaniMotors.service.comentario;

import java.util.List;

import com.example.uade.tpo.FelsaniMotors.dto.response.ComentarioResponse;

public interface ComentarioService {

    //Crea un nuevo comentario para una publicacion
    ComentarioResponse crearComentario(Long idPublicacion, Long idUsuario, String texto);

    //Crea una respuesta a un comentario existente
    ComentarioResponse crearRespuesta(Long idPublicacion, Long idComentarioPadre, Long idUsuario, String texto);

    //Obtiene un comentario por su ID
    ComentarioResponse buscarPorId(Long idComentario);

    //Lista todos los comentarios principales (sin padre) de una publicación
    List<ComentarioResponse> listarComentariosPrincipales(Long idPublicacion);

    //Lista todas las respuestas a un comentario
    List<ComentarioResponse> listarRespuestas(Long idComentarioPadre);

    //Lista todos los comentarios de una publicación organizados jerárquicamente
    //(comentarios principales seguidos por sus respuestas)
    List<ComentarioResponse> listarComentariosOrdenados(Long idPublicacion);

    //Actualiza el texto de un comentario
    ComentarioResponse actualizarTexto(Long idComentario, String nuevoTexto);

    //Elimina un comentario y todas sus respuestas
    //Solo puede eliminar el creador del comentario o un ADMIN
    void eliminarComentario(Long idComentario, Long idUsuarioActual);
}
