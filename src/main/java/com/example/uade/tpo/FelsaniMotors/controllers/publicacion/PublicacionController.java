package com.example.uade.tpo.FelsaniMotors.controllers.publicacion;

import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.PublicacionUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.PublicacionResponse;
import com.example.uade.tpo.FelsaniMotors.service.publicacion.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    
    @GetMapping("/{id}")
    public ResponseEntity<PublicacionResponse> getPublicacionById(@PathVariable Long id) {
        Optional<PublicacionResponse> publicacion = publicacionService.getPublicacionById(id);
        
        if (publicacion.isPresent()) {
            return ResponseEntity.ok(publicacion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PublicacionResponse>> getPublicacionesByUsuario(@PathVariable Long idUsuario) {
        List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByIdUsuario(idUsuario);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    

    @GetMapping("/search")
    public ResponseEntity<Page<PublicacionResponse>> searchPublicaciones(
            @RequestParam String query,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Pageable pageable;

        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
                
        Page<PublicacionResponse> publicaciones = publicacionService.buscarPublicaciones(query, pageable);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/precio")
    public ResponseEntity<List<PublicacionResponse>> getPublicacionesByRangoPrecio(
            @RequestParam float min,
            @RequestParam float max) {
        
        List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByRangoPrecio(min, max);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PublicacionResponse>> getPublicacionesByEstado(@PathVariable char estado) {
        List<PublicacionResponse> publicaciones = publicacionService.getPublicacionesByEstado(estado);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    // --- Seccion POST --- //
    
    @PostMapping
    public ResponseEntity<PublicacionResponse> createPublicacion(@RequestBody PublicacionCreateRequest createRequest) {
        PublicacionResponse nuevaPublicacion = publicacionService.createPublicacion(createRequest);
        ResponseEntity.created(URI.create("/api/publicaciones" + nuevaPublicacion.getIdPublicacion())).body(nuevaPublicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPublicacion);
    }
    
    // --- Seccion PUT --- //
    
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionResponse> updatePublicacion(
            @PathVariable Long id,
            @RequestBody PublicacionUpdateRequest updateRequest) {
        
        try {
            PublicacionResponse publicacionActualizada = publicacionService.updatePublicacion(id, updateRequest);
            return ResponseEntity.ok(publicacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PublicacionResponse> updateEstadoPublicacion(
            @PathVariable Long id,
            @RequestParam char estado) {
        
        try {
            PublicacionResponse publicacionActualizada = publicacionService.updateEstadoPublicacion(id, estado);
            return ResponseEntity.ok(publicacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // --- Seccion DELETE --- //
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicacion(@PathVariable Long id) {
        boolean eliminado = publicacionService.deletePublicacion(id);
        
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}