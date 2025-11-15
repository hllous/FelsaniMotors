package com.example.uade.tpo.FelsaniMotors.controllers;

import com.example.uade.tpo.FelsaniMotors.service.catalogo.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller para manejar catálogos y enums de datos estáticos
 * utilizados en formularios y validaciones del sistema.
 * 
 * Endpoints disponibles:
 * - GET /api/catalogos/marcas - Devuelve marcas de autos disponibles
 * - GET /api/catalogos/estados - Devuelve estados de autos (Nuevo/Usado)
 * - GET /api/catalogos/combustibles - Devuelve tipos de combustible
 * - GET /api/catalogos/tipos-caja - Devuelve tipos de caja de cambios
 */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/marcas")
    public ResponseEntity<List<String>> getMarcas() {
        return ResponseEntity.ok(catalogoService.getMarcas());
    }

    @GetMapping("/estados")
    public ResponseEntity<List<String>> getEstados() {
        return ResponseEntity.ok(catalogoService.getEstados());
    }

    @GetMapping("/combustibles")
    public ResponseEntity<List<String>> getCombustibles() {
        return ResponseEntity.ok(catalogoService.getCombustibles());
    }

    @GetMapping("/tipos-caja")
    public ResponseEntity<List<String>> getTiposCaja() {
        return ResponseEntity.ok(catalogoService.getTiposCaja());
    }
}
