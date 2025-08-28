package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import com.example.uade.tpo.FelsaniMotors.entity.dto.PublicacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PublicacionService {
    
    // --- Seccion GET --- //
    
    /**
     * Obtiene todas las publicaciones paginadas
     */
    Page<PublicacionDTO> getAllPublicaciones(Pageable pageable);
    
    /**
     * Busca una publicación por su ID
     */
    Optional<PublicacionDTO> getPublicacionById(Long id);
    
    /**
     * Obtiene las publicaciones de un usuario específico
     */
    List<PublicacionDTO> getPublicacionesByUsuario(Long idUsuario);
    
    /**
     * Obtiene las publicaciones de un auto específico
     */
    List<PublicacionDTO> getPublicacionesByAuto(Long idAuto);
    
    /**
     * Busca publicaciones por término de búsqueda
     */
    Page<PublicacionDTO> buscarPublicaciones(String busqueda, Pageable pageable);
    
    /**
     * Obtiene publicaciones por rango de precio
     */
    List<PublicacionDTO> getPublicacionesByRangoPrecio(float precioMin, float precioMax);
    
    /**
     * Obtiene publicaciones por estado
     */
    List<PublicacionDTO> getPublicacionesByEstado(char estado);
    
    // --- Seccion POST --- //
    
    /**
     * Crea una nueva publicación a partir de un DTO
     */
    PublicacionDTO createPublicacion(PublicacionDTO publicacionDTO);
    
    // --- Seccion PUT --- //
    
    /**
     * Actualiza una publicación existente a partir de un DTO
     */
    PublicacionDTO updatePublicacion(Long id, PublicacionDTO publicacionDTO);
    
    /**
     * Actualiza el estado de una publicación
     */
    PublicacionDTO updateEstadoPublicacion(Long id, char estado);
    
    /**
     * Elimina una publicación
     */
    boolean deletePublicacion(Long id);
    
    // --- Métodos internos para trabajar con entidades --- //
    
    /**
     * Convierte una Publicacion a PublicacionDTO
     */
    PublicacionDTO convertToDto(Publicacion publicacion);
    
    /**
     * Convierte un PublicacionDTO a Publicacion
     */
    Publicacion convertToEntity(PublicacionDTO dto);
}