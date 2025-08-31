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
    

    // Encuentro publicaciones por el ID del usuario

    @Query("SELECT p FROM Publicacion p WHERE p.usuario.id = :idUsuario")
    List<Publicacion> findByIdUsuario(@Param("idUsuario") Long idUsuario);

    // Busco publicaciones por titulo, ubicacion o descripcion similar a un texto

    @Query("SELECT p FROM Publicacion p WHERE " +
           "LOWER(p.titulo) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.ubicacion) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    Page<Publicacion> buscarPublicaciones(@Param("busqueda") String busqueda, Pageable pageable);
    
    // Encuentro publicaciones por rango de precio

    List<Publicacion> findByPrecioBetween(float precioMin, float precioMax);
    
    // Encuentro publicaciones por estado

    List<Publicacion> findByEstado(char estado);
    
    // Obtengo todas las publicaciones ordenadas por fecha de publicacion, de manera descendente

    Page<Publicacion> findAllByOrderByFechaPublicacionDesc(Pageable pageable);
}