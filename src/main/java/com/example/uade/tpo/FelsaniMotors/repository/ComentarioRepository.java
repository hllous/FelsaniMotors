package com.example.uade.tpo.FelsaniMotors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // Comentarios raiz (sin padre)
    List<Comentario> findByComentarioPadreIsNullOrderByFechaAsc();

    // Respuestas de un comentario
    List<Comentario> findByComentarioPadreIdComentarioOrderByFechaAsc(Long idComentarioPadre);

}