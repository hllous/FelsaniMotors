package com.example.uade.tpo.FelsaniMotors.controllers;

import com.example.uade.tpo.FelsaniMotors.dto.request.TransaccionCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.TransaccionUpdateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.TransaccionResponse;
import com.example.uade.tpo.FelsaniMotors.service.transaccion.TransaccionService;
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
 * - getAllTransacciones = Devuelve todas las transacciones
 * - getTransaccionById = Devuelve una transaccion por su ID
 * - getTransaccionesByIdPublicacion = Devuelve transacciones relacionadas con una publicacion
 * 
 * POST
 * 
 * - crearTransaccion = Crea una nueva transaccion
 * 
 * PUT
 * 
 * - updateTransaccion = Actualiza el estado y otros parametros
 * 
 * DELETE
 * 
 * - deleteTransaccion = Elimina transaccion
 */

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    // --- Seccion GET --- //
    
    @GetMapping
    public ResponseEntity<Page<TransaccionResponse>> getAllTransacciones(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageable = PageRequest.of(page, size);
        }
        
        Page<TransaccionResponse> transaccionesPage = transaccionService.getAllTransacciones(pageable);
        
        if (transaccionesPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transaccionesPage);
        }
    }
    
    @GetMapping("/{idTransaccion}")
    public ResponseEntity<TransaccionResponse> getTransaccionById(@PathVariable Long idTransaccion) {
        Optional<TransaccionResponse> transaccion = transaccionService.getTransaccionById(idTransaccion);
        
        if (transaccion.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transaccion.get());
        }
    }
    
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<TransaccionResponse>> getTransaccionesByIdPublicacion(
            @PathVariable Long idPublicacion) {
        
        List<TransaccionResponse> transacciones = transaccionService.getTransaccionesByIdPublicacion(idPublicacion);
        
        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transacciones);
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<TransaccionResponse>> getTransaccionesByUsuario(
            @PathVariable Long idUsuario) {
        
        List<TransaccionResponse> transacciones = transaccionService.getTransaccionesByUsuario(idUsuario);
        
        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(transacciones);
        }
    }
    
    // --- Seccion POST --- //
    
    @PostMapping
    public ResponseEntity<TransaccionResponse> crearTransaccion(@RequestBody TransaccionCreateRequest request) {
        
        TransaccionResponse nuevaTransaccion = transaccionService.crearTransaccion(
            request.getIdPublicacion(),
            request.getIdComprador(),
            request.getMonto(),
            request.getMetodoPago(),
            request.getReferenciaPago(),
            request.getComentarios()
        );
        
        return ResponseEntity.created(URI.create("/api/transacciones/" + nuevaTransaccion.getIdTransaccion()))
                         .body(nuevaTransaccion);
    }

    // --- Seccion PUT --- //
    
    @PutMapping("/{idTransaccion}")
    public ResponseEntity<TransaccionResponse> updateTransaccion(
            @PathVariable Long idTransaccion,
            @RequestBody TransaccionUpdateRequest request,
            Authentication authentication) {
        
        TransaccionResponse transaccionActualizada = transaccionService.updateTransaccion(
            idTransaccion,
            request.getEstado(),
            request.getReferenciaPago(),
            request.getComentarios(),
            authentication
        );
        return ResponseEntity.ok(transaccionActualizada);
    }

    // --- Seccion DELETE --- //
    
    @DeleteMapping("/{idTransaccion}")
    public ResponseEntity<Void> deleteTransaccion(
            @PathVariable Long idTransaccion,
            Authentication authentication) {
        
        boolean eliminado = transaccionService.deleteTransaccion(idTransaccion, authentication);
        
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
