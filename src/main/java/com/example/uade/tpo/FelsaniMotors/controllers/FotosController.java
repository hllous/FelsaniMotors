package com.example.uade.tpo.FelsaniMotors.controllers;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.dto.request.FotoUploadRequest;
import com.example.uade.tpo.FelsaniMotors.dto.response.FotoResponse;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.service.foto.FotoService;

/* 
Metodos CRUD:

GET:
getFotos: Devuelve las fotos de una publicacion, con opcion de incluir o no las imagenes
getFotoById: Devuelve una foto especifica, con opcion de incluir o no la imagen
getFotosContenido: Devuelve solo el contenido de las imagenes en base64

POST:
uploadFoto: Permite subir una nueva foto a una publicacion

DELETE:
deleteFoto: Borra la foto por ID

PUT:
setMainFoto: Marca esa foto como la principal de la publicacion
updateFotoOrder: Actualiza el orden de la foto
*/

@RestController
@CrossOrigin
public class FotosController {

    @Autowired
    private FotoService fotoService;

    @GetMapping("/api/publicaciones/{idPublicacion}/fotos")
    public ResponseEntity<Object> getFotos(
            @PathVariable Long idPublicacion,
            @RequestParam(required = false, defaultValue = "false") Boolean includeImages,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) throws IOException, SQLException {
        
        // Si se pide incluir las imágenes, devolvemos el formato ImageResponse
        if (includeImages) {
            return ResponseEntity.ok(fotoService.getImagesFromPublicacion(idPublicacion));
        }
        
        // Si no, devolvemos solo los metadatos
        PageRequest pageRequest;
        if (page == null || size == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageRequest = PageRequest.of(page, size);
        }
        
        Page<Foto> fotosPage = fotoService.getFotosByPublicacion(idPublicacion, pageRequest);
        List<Foto> fotos = fotosPage.getContent();
        
        if (fotos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fotos);
    }

    @PostMapping(value = "/api/publicaciones/{idPublicacion}/fotos", consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadFoto(
            @PathVariable Long idPublicacion,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) Boolean esPrincipal,
            @RequestParam(required = false) Integer orden) throws Exception {
        
        FotoUploadRequest request = new FotoUploadRequest(file, esPrincipal, orden);
        Foto result = fotoService.addFoto(idPublicacion, request);
        
        return ResponseEntity
                .created(URI.create("/api/publicaciones/" + idPublicacion + "/fotos/" + result.getIdFoto()))
                .body(result);
    }

    @GetMapping("/api/publicaciones/{idPublicacion}/fotos/{idFoto}")
    public ResponseEntity<Object> getFotoById(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto,
            @RequestParam(required = false, defaultValue = "false") Boolean includeImage) {
        try {

            if (includeImage) {
                FotoResponse response = fotoService.getFotoResponse(idFoto);
                return ResponseEntity.ok(response);
            }
            
            Foto foto = fotoService.getFotoById(idFoto);
            return ResponseEntity.ok(foto);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/publicaciones/{idPublicacion}/fotos/{idFoto}")
    public ResponseEntity<Void> deleteFoto(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto) {
        fotoService.deleteFoto(idFoto);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/api/publicaciones/{idPublicacion}/fotos/{idFoto}/principal")
    public ResponseEntity<Foto> setMainFoto(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto) {
        try {
            Foto foto = fotoService.setMainFoto(idFoto);
            return ResponseEntity.ok(foto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/api/publicaciones/{idPublicacion}/fotos/{idFoto}/orden")
    public ResponseEntity<Foto> updateFotoOrder(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto,
            @RequestParam Integer orden) {
        try {
            Foto foto = fotoService.updateFotoOrder(idFoto, orden);
            return ResponseEntity.ok(foto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/api/publicaciones/{idPublicacion}/fotos-contenido")
    public ResponseEntity<Object> getFotosContenido(
            @PathVariable Long idPublicacion) throws IOException, SQLException {
        return ResponseEntity.ok(fotoService.getImagesFromPublicacion(idPublicacion));
    }
}