package com.example.uade.tpo.FelsaniMotors.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade.tpo.FelsaniMotors.dto.response.ComentarioResponse;
import com.example.uade.tpo.FelsaniMotors.service.comentario.ComentarioService;

/*
     Endpoints ADMIN para comentarios

     GET:
         - listarTodosLosComentarios: Lista todos los comentarios del sistema (ADMIN only)
*/

@RestController
@RequestMapping("/api/comentarios/admin")
public class AdminComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    // ---Seccion GET --- //

    @GetMapping("/all")
    public ResponseEntity<List<ComentarioResponse>> listarTodosLosComentarios() {
        List<ComentarioResponse> comentarios = comentarioService.listarTodosLosComentarios();
        
        if (comentarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(comentarios);
    }
}
