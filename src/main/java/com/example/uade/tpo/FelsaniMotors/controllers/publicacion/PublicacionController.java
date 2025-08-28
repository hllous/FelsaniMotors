package com.example.uade.tpo.FelsaniMotors.controllers.publicacion;

import com.example.uade.tpo.FelsaniMotors.entity.dto.PublicacionDTO;
import com.example.uade.tpo.FelsaniMotors.service.publicacion.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    // --- Seccion GET --- //
    
    @GetMapping
    public ResponseEntity<Page<PublicacionDTO>> getAllPublicaciones(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        
        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
            
        Page<PublicacionDTO> publicaciones = publicacionService.getAllPublicaciones(pageable);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> getPublicacionById(@PathVariable Long id) {
        Optional<PublicacionDTO> publicacion = publicacionService.getPublicacionById(id);
        
        if (publicacion.isPresent()) {
            return ResponseEntity.ok(publicacion.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PublicacionDTO>> getPublicacionesByUsuario(@PathVariable Long idUsuario) {
        List<PublicacionDTO> publicaciones = publicacionService.getPublicacionesByUsuario(idUsuario);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/auto/{idAuto}")
    public ResponseEntity<List<PublicacionDTO>> getPublicacionesByAuto(@PathVariable Long idAuto) {
        List<PublicacionDTO> publicaciones = publicacionService.getPublicacionesByAuto(idAuto);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<PublicacionDTO>> searchPublicaciones(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PublicacionDTO> publicaciones = publicacionService.buscarPublicaciones(query, pageable);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/precio")
    public ResponseEntity<List<PublicacionDTO>> getPublicacionesByRangoPrecio(
            @RequestParam float min,
            @RequestParam float max) {
        
        List<PublicacionDTO> publicaciones = publicacionService.getPublicacionesByRangoPrecio(min, max);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PublicacionDTO>> getPublicacionesByEstado(@PathVariable char estado) {
        List<PublicacionDTO> publicaciones = publicacionService.getPublicacionesByEstado(estado);
        
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(publicaciones);
        }
    }
    
    // --- Seccion POST --- //
    
    @PostMapping
    public ResponseEntity<PublicacionDTO> createPublicacion(@RequestBody PublicacionDTO publicacionDTO) {
        PublicacionDTO nuevaPublicacion = publicacionService.createPublicacion(publicacionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPublicacion);
    }
    
    // --- Seccion PUT --- //
    
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDTO> updatePublicacion(
            @PathVariable Long id,
            @RequestBody PublicacionDTO publicacionDTO) {
        
        try {
            PublicacionDTO publicacionActualizada = publicacionService.updatePublicacion(id, publicacionDTO);
            return ResponseEntity.ok(publicacionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PublicacionDTO> updateEstadoPublicacion(
            @PathVariable Long id,
            @RequestParam char estado) {
        
        try {
            PublicacionDTO publicacionActualizada = publicacionService.updateEstadoPublicacion(id, estado);
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