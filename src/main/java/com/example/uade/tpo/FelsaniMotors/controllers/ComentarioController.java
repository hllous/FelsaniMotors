package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade.tpo.FelsaniMotors.entity.Comentario;
import com.example.uade.tpo.FelsaniMotors.service.ComentarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<Comentario> crearComentario(@Valid @RequestBody CreateComentarioRequest request) {
        Comentario nuevo = new Comentario();
        nuevo.setTexto(request.getTexto());
        nuevo.setFecha(new Date());
        Comentario creado = comentarioService.crear(nuevo);

        return ResponseEntity
                .created(URI.create("/comentarios/" + creado.getIdComentario()))
                .body(creado);
    }

    @PostMapping("/{id}/respuestas")
    public ResponseEntity<Comentario> responder(
            @PathVariable Long id,
            @Valid @RequestBody CreateComentarioRequest request) {

        Comentario respuesta = new Comentario();
        respuesta.setTexto(request.getTexto());
        respuesta.setFecha(new Date());

        Comentario creado = comentarioService.responder(id, respuesta);

        return ResponseEntity
                .created(URI.create("/comentarios/" + creado.getIdComentario()))
                .body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> getComentario(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> listarRaiz() {
        return ResponseEntity.ok(comentarioService.listarRaiz());
    }

    @GetMapping("/{id}/respuestas")
    public ResponseEntity<List<Comentario>> listarRespuestas(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.listarRespuestas(id));
    }

    @PutMapping("/{id}/texto")
    public ResponseEntity<Comentario> actualizarTexto(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTextoRequest request) {

        return ResponseEntity.ok(comentarioService.actualizarTexto(id, request.getTexto()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comentarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    static class CreateComentarioRequest {
        @NotBlank
        private String texto;

        public String getTexto() { return texto; }
        public void setTexto(String texto) { this.texto = texto; }
    }

    static class UpdateTextoRequest {
        @NotBlank
        private String texto;

        public String getTexto() { return texto; }
        public void setTexto(String texto) { this.texto = texto; }
    }
}
