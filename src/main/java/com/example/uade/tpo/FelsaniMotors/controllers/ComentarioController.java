package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
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

import com.example.uade.tpo.FelsaniMotors.dto.request.ComentarioCreateRequest;
import com.example.uade.tpo.FelsaniMotors.dto.request.ComentarioUpdateTextoRequest;
import com.example.uade.tpo.FelsaniMotors.entity.Comentario;
import com.example.uade.tpo.FelsaniMotors.service.comentario.ComentarioService;

/*
     Metodos CRUD

     GET:
         - obtenerComentario: Obtener un comentario por su ID.
         - listarComentariosPrincipales: Listar todos los comentarios principales de una publicacion.
         - listarComentariosJerarquicamente: Listar todos los comentarios de una publicacion en orden jerarquico.
         - listarRespuestas: Listar todas las respuestas a un comentario especifico.

    POST:
         - crearComentario: Crear un nuevo comentario en una publicacion.
         - crearRespuesta: Crear una respuesta a un comentario existente.
    PUT:
         - actualizarTexto: Actualizar el texto de un comentario existente.
    DELETE:
         - eliminarComentario: Eliminar un comentario por su ID.

*/

@RestController
@RequestMapping("/api/publicaciones/{idPublicacion}/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;


    // ---Seccion GET --- //

    @GetMapping("/{idComentario}")
    public ResponseEntity<Comentario> obtenerComentario(
            @PathVariable Long idPublicacion, 
            @PathVariable Long idComentario) {
        return ResponseEntity.ok(comentarioService.buscarPorId(idComentario));
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> listarComentariosPrincipales(@PathVariable Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.listarComentariosPrincipales(idPublicacion));
    }

    @GetMapping("/jerarquicos")
    public ResponseEntity<List<Comentario>> listarComentariosJerarquicamente(@PathVariable Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.listarComentariosOrdenados(idPublicacion));
    }

    @GetMapping("/{idComentario}/respuestas")
    public ResponseEntity<List<Comentario>> listarRespuestas(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario) {
        return ResponseEntity.ok(comentarioService.listarRespuestas(idComentario));
    }

    // ---Seccion POST--- //
    @PostMapping
    public ResponseEntity<Comentario> crearComentario(
            @PathVariable Long idPublicacion,
            @RequestBody ComentarioCreateRequest request) {
        Comentario comentarioCreado = comentarioService.crearComentario(
            idPublicacion, 
            request.getIdUsuario(), 
            request.getTexto()
        );

        return ResponseEntity
                .created(URI.create("/api/publicaciones/" + idPublicacion + "/comentarios/" + comentarioCreado.getIdComentario()))
                .body(comentarioCreado);
    }

    @PostMapping("/{idComentario}/respuestas")
    public ResponseEntity<Comentario> crearRespuesta(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario,
            @RequestBody ComentarioCreateRequest request) {

        Comentario creado = comentarioService.crearRespuesta(
            idPublicacion, 
            idComentario, 
            request.getIdUsuario(), 
            request.getTexto()
        );

        return ResponseEntity
                .created(URI.create("/api/publicaciones/" + idPublicacion + "/comentarios/" + creado.getIdComentario()))
                .body(creado);
    }

    //---Seccion PUT--- //
    @PutMapping("/{idComentario}/texto")
    public ResponseEntity<Comentario> actualizarTexto(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario,
            @RequestBody ComentarioUpdateTextoRequest request) {

        return ResponseEntity.ok(comentarioService.actualizarTexto(idComentario, request.getTexto()));
    }

    //---Seccion DELETE--- //
    @DeleteMapping("/{idComentario}")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario) {
        comentarioService.eliminarComentario(idComentario);
        return ResponseEntity.noContent().build();
    }
}
