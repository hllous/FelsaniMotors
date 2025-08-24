package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.service.FotoService;

@RestController
@RequestMapping("publicaciones")
public class FotosController {

    @Autowired
    private FotoService fotoService;

    
    @GetMapping("/{publicacionId}/fotos")
    public ResponseEntity<Page<Foto>> getFotos(
            @PathVariable Long publicacionId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        if (page == null || size == null) {
            return ResponseEntity.ok(
                fotoService.getFotosDePublicacion(publicacionId, PageRequest.of(0, Integer.MAX_VALUE))
            );
        }
        return ResponseEntity.ok(
            fotoService.getFotosDePublicacion(publicacionId, PageRequest.of(page, size))
        );
    }

    
    @PostMapping(value = "/{publicacionId}/fotos", consumes = "multipart/form-data")
    public ResponseEntity<Object> subirFoto(
            @PathVariable Long publicacionId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) Boolean esPrincipal,
            @RequestParam(required = false) Integer orden) throws Exception {
        Foto result = fotoService.agregarFoto(publicacionId, file, esPrincipal, orden);
        return ResponseEntity
                .created(URI.create("/publicaciones/" + publicacionId + "/fotos/" + result.getId()))
                .body(result);
    }

    
    @GetMapping("/fotos/{fotoId}")
    public ResponseEntity<Foto> getFotoById(@PathVariable Long fotoId) {
        Optional<Foto> result = fotoService.getFotoById(fotoId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

   
    @DeleteMapping("/fotos/{fotoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long fotoId) {
        fotoService.eliminarFoto(fotoId);
        return ResponseEntity.noContent().build();
    }
}
