package com.example.uade.tpo.FelsaniMotors.service.publicacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.example.uade.tpo.FelsaniMotors.dto.response.FiltrosOpcionesResponse;
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
    
    // Obtengo publicaciones por rango de precio
    List<PublicacionResponse> getPublicacionesByRangoPrecio(float precioMin, float precioMax);
    
    // Obtebgi publicaciones por estado
    List<PublicacionResponse> getPublicacionesByEstado(char estado);
    
    // Filtrar publicaciones con criterios dinámicos (incluye búsqueda de texto)
    Page<PublicacionResponse> filtrarPublicaciones(
        String busqueda,
        List<String> marcas,
        List<String> modelos,
        List<String> anios,
        List<String> estados,
        List<String> kilometrajes,
        List<String> combustibles,
        List<String> tipoCategorias,
        List<String> tipoCajas,
        List<String> motores,
        Pageable pageable
    );
    
    // Obtener opciones disponibles para filtros
    FiltrosOpcionesResponse getOpcionesFiltros();
    
    // --- Seccion POST --- //
    
    // Creo una nueva publicacion
    PublicacionResponse createPublicacion(Long idUsuario, Long idAuto, String titulo, String descripcion, 
                                         String ubicacion, float precio, String metodoDePago);
    
    // --- Seccion PUT --- //
    
    //Actualizo una publicacion existente
    PublicacionResponse updatePublicacion(Long idPublicacion, String titulo, String descripcion, 
                                         String ubicacion, float precio, Integer descuentoPorcentaje, 
                                         String metodoDePago, Authentication authentication);
    
    // Actualizo solo el estado de una publicacion
    PublicacionResponse updateEstadoPublicacion(Long idPublicacion, char estado, Authentication authentication);

    // --- Seccion DELETE --- //
    
    // Elimino una publicacion
    boolean deletePublicacion(Long idPublicacion, Authentication authentication);
}