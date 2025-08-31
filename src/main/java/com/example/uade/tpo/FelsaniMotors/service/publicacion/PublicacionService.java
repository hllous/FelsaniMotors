package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;

import java.util.List;
import java.util.Optional;

public interface PublicacionService {
    
    // --- Seccion GET --- //
    
    // Obtiene todas las publicaciones paginadas
    Page<PublicacionResponse> getAllPublicaciones(Pageable pageable);
    
    // Busca una publicacion por su ID
    Optional<PublicacionResponse> getPublicacionById(Long id);
    
    // Obtiene publicaciones de un usuario especifico
    List<PublicacionResponse> getPublicacionesByIdUsuario(Long idUsuario);
    
    // Busca publicaciones por termino de busqueda
    Page<PublicacionResponse> buscarPublicaciones(String busqueda, Pageable pageable);
    
    // Obtiene publicaciones por rango de precio
    List<PublicacionResponse> getPublicacionesByRangoPrecio(float precioMin, float precioMax);
    
    // Obtiene publicaciones por estado
    List<PublicacionResponse> getPublicacionesByEstado(char estado);
    
    // --- Seccion POST --- //
    
    // Crea una nueva publicacion (usando parámetros individuales)
    PublicacionResponse createPublicacion(Long idUsuario, Long idAuto, String titulo, String descripcion, 
                                         String ubicacion, float precio, String metodoDePago,
                                         String urlImagen, Boolean esPrincipal, Integer orden);
    
    // --- Seccion PUT --- //
    
    //Actualiza una publicacion existente (usando parámetros individuales)
    PublicacionResponse updatePublicacion(Long idPublicacion, String titulo, String descripcion, 
                                         String ubicacion, float precio, String metodoDePago, Long idUsuario);
    
    // Actualiza el estado de una publicacion
    PublicacionResponse updateEstadoPublicacion(Long idPublicacion, char estado, Long idUsuario);

    // --- Seccion DELETE --- //
    
    // Elimina una publicacion
    boolean deletePublicacion(Long idPublicacion, Long idUsuario);
}