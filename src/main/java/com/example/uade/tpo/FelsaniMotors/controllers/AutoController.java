package com.example.uade.tpo.FelsaniMotors.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.exceptions.AutoDuplicateException;
import com.example.uade.tpo.FelsaniMotors.service.auto.AutoService;

/*
    Metodos CRUD
    GET:
        - getAutos: Obtener una lista de autos.
        - getAutoByIdAuto: Obtener un auto por su ID.
    POST:
        - createAuto: Crear un nuevo auto.
 */


@RestController
@RequestMapping("/api/autos")
public class AutoController {

    @Autowired
    private AutoService autoService;

    // ---Seccion GET--- //
    @GetMapping
    public ResponseEntity<List<Auto>> getAutos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        PageRequest pageRequest;
        if (page == null || size == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        } else {
            pageRequest = PageRequest.of(page, size);
        }

        Page<Auto> autosPage = autoService.getAutos(pageRequest);
        List<Auto> autos = autosPage.getContent();
        
        if (autos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(autos);
    }
    @GetMapping("/{idAuto}")
    public ResponseEntity<Auto> getAutoByIdAuto(@PathVariable Long idAuto) {
        Optional<Auto> result = autoService.getAutoById(idAuto);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ---Seccion POST--- //
    @PostMapping
    public ResponseEntity<Auto> createAuto(@RequestBody Auto autoRequest)
            throws AutoDuplicateException {
        Auto created = autoService.createAuto(autoRequest);
        return ResponseEntity
                .created(URI.create("/autos/" + created.getIdAuto()))
                .body(created);
    }

    // ---Seccion DELETE--- //
    @DeleteMapping("/{idAuto}")
    public ResponseEntity<Void> deleteAuto(@PathVariable Long idAuto) {
        boolean deleted = autoService.deleteAuto(idAuto);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
