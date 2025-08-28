package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;

import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PublicacionService {
    
    // --- Seccion GET --- //
    
    /**
     * Obtiene todas las publicaciones paginadas
     */
    Page<PublicacionResponse> getAllPublicaciones(Pageable pageable);
    
    /**
     * Busca una publicación por su ID
     */
    Optional<PublicacionResponse> getPublicacionById(Long id);
    
    /**
     * Obtiene las publicaciones de un usuario específico (por ID)
     */
    List<PublicacionResponse> getPublicacionesByIdUsuario(Long idUsuario);
    
    /**
     * Busca publicaciones por término de búsqueda
     */
    Page<PublicacionResponse> buscarPublicaciones(String busqueda, Pageable pageable);
    
    /**
     * Obtiene publicaciones por rango de precio
     */
    List<PublicacionResponse> getPublicacionesByRangoPrecio(float precioMin, float precioMax);
    
    /**
     * Obtiene publicaciones por estado
     */
    List<PublicacionResponse> getPublicacionesByEstado(char estado);
    
    // --- Seccion POST --- //
    
    /**
     * Crea una nueva publicación a partir de un DTO de creacion
     */
    PublicacionResponse createPublicacion(PublicacionCreateRequest createRequest);
    
    // --- Seccion PUT --- //
    
    /**
     * Actualiza una publicación existente a partir de un DTO de actualizacion
     */
    PublicacionResponse updatePublicacion(Long id, PublicacionUpdateRequest updateRequest);
    
    /**
     * Actualiza el estado de una publicación
     */
    PublicacionResponse updateEstadoPublicacion(Long id, char estado);
    
    /**
     * Elimina una publicación
     */
    boolean deletePublicacion(Long id);
    
    // --- Métodos internos para trabajar con entidades --- //
    
    /**
     * Convierte una Publicacion a PublicacionResponse
     */
    PublicacionResponse convertToDto(Publicacion publicacion);
    
    /**
     * Convierte un DTO de creacion a Publicacion
     */
    Publicacion convertToEntity(PublicacionCreateRequest createRequest);
}