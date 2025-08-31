package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.service.foto.FotoService;

@RestController
@RequestMapping("/api/publicaciones")
public class FotosController {

    @Autowired
    private FotoService fotoService;

    
    @GetMapping("/{idPublicacion}/fotos")
    public ResponseEntity<Page<Foto>> getFotos(
            @PathVariable Long idPublicacion,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            return ResponseEntity.ok(
                fotoService.getFotosByPublicacion(idPublicacion, PageRequest.of(0, Integer.MAX_VALUE))
            );
        }
        return ResponseEntity.ok(
            fotoService.getFotosByPublicacion(idPublicacion, PageRequest.of(page, size))
        );
    }

    
    @PostMapping(value = "/{idPublicacion}/fotos", consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadFoto(
            @PathVariable Long idPublicacion,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) Boolean esPrincipal,
            @RequestParam(required = false) Integer orden) throws Exception {
        Foto result = fotoService.addFoto(idPublicacion, file, esPrincipal, orden);
        return ResponseEntity
                .created(URI.create("/publicaciones/" + idPublicacion + "/fotos/" + result.getIdFoto()))
                .body(result);
    }

    
    @GetMapping("/{idPublicacion}/fotos/{idFoto}")
    public ResponseEntity<Foto> getFotoById(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto) {
        try {
            Foto foto = fotoService.getFotoById(idFoto);
            return ResponseEntity.ok(foto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una foto
     */
    @DeleteMapping("/{idPublicacion}/fotos/{idFoto}")
    public ResponseEntity<Void> deleteFoto(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto) {
        fotoService.deleteFoto(idFoto);
        return ResponseEntity.noContent().build();
    }
    
 
    @GetMapping("/{idPublicacion}/fotos/{idFoto}/imagen")
    public ResponseEntity<byte[]> getFotoImage(
            @PathVariable Long idPublicacion,
            @PathVariable Long idFoto) {
        try {
            byte[] datos = fotoService.getFotoData(idFoto);
            
            HttpHeaders headers = new HttpHeaders();
            
            // Por defecto, asumimos que es una imagen JPEG
            headers.setContentType(MediaType.IMAGE_JPEG);
            
            return new ResponseEntity<>(datos, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Marca una foto como principal para una publicación
     */
    @PutMapping("/{idPublicacion}/fotos/{idFoto}/principal")
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
    
    /**
     * Actualiza el orden de una foto
     */
    @PutMapping("/{idPublicacion}/fotos/{idFoto}/orden")
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
}
