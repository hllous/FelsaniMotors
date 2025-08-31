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

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    // --- Seccion GET --- //
    
    @GetMapping
    public ResponseEntity<Page<PublicacionResponse>> getAllPublicaciones(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Character estado,
            @RequestParam(required = false) Float precioMin,
            @RequestParam(required = false) Float precioMax,
            @RequestParam(required = false) String busqueda) {
        
        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
        
        // Si hay algún filtro específico, aplicarlo
        if (idUsuario != null) {
            List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByIdUsuario(idUsuario);
            if (publicaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                // Convertir lista a Page para mantener la consistencia de la respuesta
                Page<PublicacionResponse> pageResult = new org.springframework.data.domain.PageImpl<>(publicaciones);
                return ResponseEntity.ok(pageResult);
            }
        } else if (estado != null) {
            List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByEstado(estado);
            if (publicaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                // Convertir lista a Page para mantener la consistencia de la respuesta
                Page<PublicacionResponse> pageResult = new org.springframework.data.domain.PageImpl<>(publicaciones);
                return ResponseEntity.ok(pageResult);
            }
        } else if (precioMin != null && precioMax != null) {
            List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByRangoPrecio(precioMin, precioMax);
            if (publicaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                // Convertir lista a Page para mantener la consistencia de la respuesta
                Page<PublicacionResponse> pageResult = new org.springframework.data.domain.PageImpl<>(publicaciones);
                return ResponseEntity.ok(pageResult);
            }
        } else if (busqueda != null && !busqueda.trim().isEmpty()) {
            Page<PublicacionResponse> publicaciones = publicacionService.buscarPublicaciones(busqueda, pageable);
            if (publicaciones.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(publicaciones);
            }
        }
            
        // Si no hay filtros, devolver todas las publicaciones
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
        
        if (publicacion.isPresent()) {
            return ResponseEntity.ok(publicacion.get());
        } else {
            return ResponseEntity.notFound().build();
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