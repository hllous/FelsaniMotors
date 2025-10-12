package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.example.uade.tpo.FelsaniMotors.dto.response.ComentarioResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Usuario;
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
    public ResponseEntity<ComentarioResponse> obtenerComentario(
            @PathVariable Long idPublicacion, 
            @PathVariable Long idComentario) {
        ComentarioResponse comentario = comentarioService.buscarPorId(idComentario);
        return ResponseEntity.ok(comentario);
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponse>> listarComentariosPrincipales(@PathVariable Long idPublicacion) {
        List<ComentarioResponse> comentarios = comentarioService.listarComentariosPrincipales(idPublicacion);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/jerarquicos")
    public ResponseEntity<List<ComentarioResponse>> listarComentariosJerarquicamente(@PathVariable Long idPublicacion) {
        List<ComentarioResponse> comentarios = comentarioService.listarComentariosOrdenados(idPublicacion);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/{idComentario}/respuestas")
    public ResponseEntity<List<ComentarioResponse>> listarRespuestas(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario) {
        List<ComentarioResponse> respuestas = comentarioService.listarRespuestas(idComentario);
        return ResponseEntity.ok(respuestas);
    }

    // ---Seccion POST--- //
    @PostMapping
    public ResponseEntity<ComentarioResponse> crearComentario(
            @PathVariable Long idPublicacion,
            @RequestBody ComentarioCreateRequest request) {
        ComentarioResponse comentarioCreado = comentarioService.crearComentario(
            idPublicacion, 
            request.getIdUsuario(), 
            request.getTexto()
        );

        return ResponseEntity
                .created(URI.create("/api/publicaciones/" + idPublicacion + "/comentarios/" + comentarioCreado.getIdComentario()))
                .body(comentarioCreado);
    }

    @PostMapping("/{idComentario}/respuestas")
    public ResponseEntity<ComentarioResponse> crearRespuesta(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario,
            @RequestBody ComentarioCreateRequest request) {

        ComentarioResponse creado = comentarioService.crearRespuesta(
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
    public ResponseEntity<ComentarioResponse> actualizarTexto(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario,
            @RequestBody ComentarioUpdateTextoRequest request) {

        ComentarioResponse actualizado = comentarioService.actualizarTexto(idComentario, request.getTexto());
        return ResponseEntity.ok(actualizado);
    }

    //---Seccion DELETE--- //
    @DeleteMapping("/{idComentario}")
    public ResponseEntity<Void> eliminarComentario(
            @PathVariable Long idPublicacion,
            @PathVariable Long idComentario,
            Authentication authentication) {

        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        
        comentarioService.eliminarComentario(idComentario, usuarioActual.getIdUsuario());
        return ResponseEntity.noContent().build();
    }
}
