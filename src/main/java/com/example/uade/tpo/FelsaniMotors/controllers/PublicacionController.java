package com.example.uade.tpo.FelsaniMotors.controllers;

import com.example.uade.tpo.FelsaniMotors.dto.response.FiltrosOpcionesResponse;
import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;
import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionEstadoRequest;
import com.example.uade.tpo.FelsaniMotors.service.publicacion.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/*
 * Metodos CRUD
 * 
 * GET
 * 
 * - getAllPublicaciones = Devuelve todas las publicaciones
 * - getPublicacionById = Devuelve una publicacion por su id
 * - getPublicacionesByIdUsuario = Devuelve publicaciones de un usuario
 * - getPublicacionesByRangoPrecio = Devuelve publicaciones por rango de precio
 * - getPublicacionesByEstado = Devuelve publicaciones por estado
 * - filtrarPublicaciones = Filtra publicaciones con búsqueda de texto y filtros múltiples
 * - getOpcionesFiltros = Devuelve opciones disponibles para los filtros
 * 
 * POST
 * 
 * - createPublicacion = Crea una publicacion nueva
 *   Las fotos deben subirse despues de crear la publicacion usando:
 *   POST /api/publicaciones/{idPublicacion}/fotos
 * 
 * PUT
 * 
 * - updatePublicacion = Actualiza una publicacion, valida idUsuario
 * - updateEstadoPublicacion = Actualiza el estado de una publicacion, valida idUsuario
 * 
 * DELETE
 * 
 * - deletePublicacion = Elimina una publicacion, valida idUsuario
 * 
 */

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    // --- Seccion GET --- //
    
    @GetMapping
    public ResponseEntity<List<PublicacionResponse>> getAllPublicaciones(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<PublicacionResponse> publicacionesPage = publicacionService.getAllPublicaciones(pageable);
        List<PublicacionResponse> publicaciones = publicacionesPage.getContent();

        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/{idPublicacion}")
    public ResponseEntity<PublicacionResponse> getPublicacionById(@PathVariable Long idPublicacion) {
        Optional<PublicacionResponse> publicacion = publicacionService.getPublicacionById(idPublicacion);
        
        if (publicacion.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(publicacion.get());
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PublicacionResponse>> getPublicacionesByIdUsuario(@PathVariable Long idUsuario) {
        List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByIdUsuario(idUsuario);

        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/rango-precio")
    public ResponseEntity<List<PublicacionResponse>> getPublicacionesByRangoPrecio(
            @RequestParam float precioMin,
            @RequestParam float precioMax) {

        List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByRangoPrecio(precioMin, precioMax);

        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PublicacionResponse>> getPublicacionesByEstado(
            @RequestParam char estado) {

        List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByEstado(estado);

        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/filtrar")
    public ResponseEntity<Page<PublicacionResponse>> filtrarPublicaciones(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) List<String> marca,
            @RequestParam(required = false) List<String> modelo,
            @RequestParam(required = false) List<String> anio,
            @RequestParam(required = false) List<String> estado,
            @RequestParam(required = false) List<String> kilometraje,
            @RequestParam(required = false) List<String> combustible,
            @RequestParam(required = false) List<String> tipoCategoria,
            @RequestParam(required = false) List<String> tipoCaja,
            @RequestParam(required = false) List<String> motor,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, 20); // Default: 20 resultados por página
        } else {
            pageable = PageRequest.of(page, size);
        }
        
        Page<PublicacionResponse> publicaciones = publicacionService.filtrarPublicaciones(
            busqueda, marca, modelo, anio, estado, kilometraje,
            combustible, tipoCategoria, tipoCaja, motor,
            pageable
        );

        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/filtros/opciones")
    public ResponseEntity<FiltrosOpcionesResponse> getOpcionesFiltros() {
        FiltrosOpcionesResponse opciones = publicacionService.getOpcionesFiltros();
        return ResponseEntity.ok(opciones);
    }
    
    // --- Seccion POST --- //
    
    @PostMapping
    public ResponseEntity<PublicacionResponse> createPublicacion(@RequestBody PublicacionCreateRequest request) {
        
        PublicacionResponse nuevaPublicacion = publicacionService.createPublicacion(
            request.getIdUsuario(), 
            request.getIdAuto(), 
            request.getTitulo(), 
            request.getDescripcion(), 
            request.getUbicacion(), 
            request.getPrecio(), 
            request.getMetodoDePago()
        );
        
        return ResponseEntity.created(URI.create("/api/publicaciones/" + nuevaPublicacion.getIdPublicacion()))
                          .body(nuevaPublicacion);
    }
    
    // --- Seccion PUT --- //
    
    @PutMapping("/{idPublicacion}")
    public ResponseEntity<PublicacionResponse> updatePublicacion(
            @PathVariable Long idPublicacion,
            @RequestBody PublicacionUpdateRequest request,
            Authentication authentication) {
        
        PublicacionResponse publicacionActualizada = publicacionService.updatePublicacion(
            idPublicacion, 
            request.getTitulo(), 
            request.getDescripcion(), 
            request.getUbicacion(), 
            request.getPrecio(),
            request.getDescuentoPorcentaje(),
            request.getMetodoDePago(), 
            authentication
        );
        return ResponseEntity.ok(publicacionActualizada);
    }
    
    @PutMapping("/{idPublicacion}/estado")
    public ResponseEntity<PublicacionResponse> updateEstadoPublicacion(
            @PathVariable Long idPublicacion,
            @RequestBody PublicacionEstadoRequest request,
            Authentication authentication) {
        
        PublicacionResponse publicacionActualizada = publicacionService.updateEstadoPublicacion(
            idPublicacion, request.getEstado(), authentication);
        return ResponseEntity.ok(publicacionActualizada);
    }
    
    // --- Seccion DELETE --- //
    
    @DeleteMapping("/{idPublicacion}")
    public ResponseEntity<Void> deletePublicacion(
            @PathVariable Long idPublicacion,
            Authentication authentication) {
        
        boolean eliminado = publicacionService.deletePublicacion(idPublicacion, authentication);
        
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}