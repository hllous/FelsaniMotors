package com.example.uade.tpo.FelsaniMotors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    /**
     * Busca comentarios principales (sin padre) de una publicación
     */
    @Query("SELECT c FROM Comentario c WHERE c.publicacion.idPublicacion = :idPublicacion AND c.padre IS NULL")
    List<Comentario> findComentariosPrincipalesByIdPublicacion(@Param("idPublicacion") Long idPublicacion);

    @Query("SELECT c FROM Comentario c WHERE c.padre.idComentario = :idPadre")
    List<Comentario> findRespuestasByPadreId(@Param("idPadre") Long idPadre);
    
    /**
     * Busca todos los comentarios de una publicación ordenados por jerarquía
     * Los comentarios principales aparecen primero, seguidos por sus respuestas
     */
    @Query("SELECT c FROM Comentario c WHERE c.publicacion.idPublicacion = :idPublicacion ORDER BY " +
           "CASE WHEN c.padre IS NULL THEN c.idComentario ELSE c.padre.idComentario END, " +
           "CASE WHEN c.padre IS NULL THEN 0 ELSE 1 END, " +
           "c.fecha ASC")
    List<Comentario> findAllComentariosByPublicacionOrdenados(@Param("idPublicacion") Long idPublicacion);


    

}