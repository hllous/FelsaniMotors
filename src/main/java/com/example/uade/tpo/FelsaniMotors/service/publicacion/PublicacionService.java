package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;

import java.util.List;
import java.util.Optional;

public interface PublicacionService {
    
    // --- Seccion GET --- //
    
    // Obtengo todas las publicaciones
    Page<PublicacionResponse> getAllPublicaciones(Pageable pageable);
    
    // Obtengo una publicacion con su id
    Optional<PublicacionResponse> getPublicacionById(Long id);
    
    // Obtengi publicaciones de un usuario especifico
    List<PublicacionResponse> getPublicacionesByIdUsuario(Long idUsuario);
    
    // Obtengo publicaciones por una busqueda de un string en el titulo, descripcion o ubicacion
    Page<PublicacionResponse> buscarPublicaciones(String busqueda, Pageable pageable);
    
    // Obtengo publicaciones por rango de precio
    List<PublicacionResponse> getPublicacionesByRangoPrecio(float precioMin, float precioMax);
    
    // Obtebgi publicaciones por estado
    List<PublicacionResponse> getPublicacionesByEstado(char estado);
    
    // --- Seccion POST --- //
    
    // Creo una nueva publicacion
    PublicacionResponse createPublicacion(Long idUsuario, Long idAuto, String titulo, String descripcion, 
                                         String ubicacion, float precio, String metodoDePago,
                                         String urlImagen, Boolean esPrincipal, Integer orden, 
                                         Long idCategoria, String tipoCategoria, String marcaCategoria);
    
    // --- Seccion PUT --- //
    
    //Actualizo una publicacion existente
    PublicacionResponse updatePublicacion(Long idPublicacion, String titulo, String descripcion, 
                                         String ubicacion, float precio, String metodoDePago, Long idUsuario);
    
    // Actualizo solo el estado de una publicacion
    PublicacionResponse updateEstadoPublicacion(Long idPublicacion, char estado, Long idUsuario);

    // --- Seccion DELETE --- //
    
    // Elimino una publicacion
    boolean deletePublicacion(Long idPublicacion, Long idUsuario);
}