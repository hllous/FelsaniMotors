package com.example.uade.tpo.FelsaniMotors.controllers;

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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/*
 * Metodos CRUD
 * 
 * GET
 * 
 * - getAllPublicaciones = Devuelve todas las publicaciones, con o sin filtros
 * - getPublicacionById = Devuelve una publicacion utilizando su id
 * - getPublicacionesByIdUsuario = Devuelve una publicacion utilizando un id de usuario
 * - buscarPublicaciones = Busca publicaciones por titulo, descripcion o ubicacion
 * - getPublicacionesByRangoPrecio
 * - getPublicacionesByEstado
 * 
 * POST
 * 
 * - createPublicacion = Crea una publicacion nueva
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
    public ResponseEntity<Page<PublicacionResponse>> getAllPublicaciones(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<PublicacionResponse> publicaciones = publicacionService.getAllPublicaciones(pageable);

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
    
    @GetMapping("/buscar")
    public ResponseEntity<Page<PublicacionResponse>> buscarPublicaciones(
            @RequestParam String busqueda,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
        
        Page<PublicacionResponse> publicaciones = publicacionService.buscarPublicaciones(busqueda, pageable);

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
            request.getMetodoDePago(), 
            request.getUrlImagen(), 
            request.getEsPrincipal(), 
            request.getOrden()
        );
        
        return ResponseEntity.created(URI.create("/api/publicaciones/" + nuevaPublicacion.getIdPublicacion()))
                          .body(nuevaPublicacion);
    }
    
    // --- Seccion PUT --- //
    
    @PutMapping("/{idPublicacion}")
    public ResponseEntity<PublicacionResponse> updatePublicacion(
            @PathVariable Long idPublicacion,
            @RequestBody PublicacionUpdateRequest request) {
        
        try {
            PublicacionResponse publicacionActualizada = publicacionService.updatePublicacion(
                idPublicacion, 
                request.getTitulo(), 
                request.getDescripcion(), 
                request.getUbicacion(), 
                request.getPrecio(), 
                request.getMetodoDePago(), 
                request.getIdUsuario()
            );
            return ResponseEntity.ok(publicacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{idPublicacion}/estado")
    public ResponseEntity<PublicacionResponse> updateEstadoPublicacion(
            @PathVariable Long idPublicacion,
            @RequestBody PublicacionEstadoRequest request) {
        
        try {
            PublicacionResponse publicacionActualizada = publicacionService.updateEstadoPublicacion(
                idPublicacion, request.getEstado(), request.getIdUsuario());
            return ResponseEntity.ok(publicacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // --- Seccion DELETE --- //
    
    @DeleteMapping("/{idPublicacion}")
    public ResponseEntity<Void> deletePublicacion(
            @PathVariable Long idPublicacion,
            @RequestParam Long idUsuario) {
        
        boolean eliminado = publicacionService.deletePublicacion(idPublicacion, idUsuario);
            
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}