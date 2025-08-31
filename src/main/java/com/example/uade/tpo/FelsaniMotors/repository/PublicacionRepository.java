package com.example.uade.tpo.FelsaniMotors.repository;


import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    
    /**
     * Encuentra publicaciones por el ID del usuario
     */
    @Query("SELECT p FROM Publicacion p WHERE p.usuario.id = :idUsuario")
    List<Publicacion> findByIdUsuario(@Param("idUsuario") Long idUsuario);

    /**
     * Busca publicaciones por título, ubicación o descripción
     */
    @Query("SELECT p FROM Publicacion p WHERE " +
           "LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.ubicacion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    Page<Publicacion> buscarPublicaciones(@Param("busqueda") String busqueda, Pageable pageable);
    
    /**
     * Encuentra publicaciones por rango de precio
     */
    List<Publicacion> findByPrecioBetween(float precioMin, float precioMax);
    
    /**
     * Encuentra publicaciones activas (por estado)
     */
    List<Publicacion> findByEstado(char estado);
    
    /**
     * Obtiene todas las publicaciones paginadas y ordenadas por fecha de publicación descendente
     */
    Page<Publicacion> findAllByOrderByFechaPublicacionDesc(Pageable pageable);
}