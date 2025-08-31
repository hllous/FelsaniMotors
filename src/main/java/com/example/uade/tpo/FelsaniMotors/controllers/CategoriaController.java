package com.example.uade.tpo.FelsaniMotors.controllers;

import com.example.uade.tpo.FelsaniMotors.entity.Categoria;
import com.example.uade.tpo.FelsaniMotors.dto.request.CategoriaRequest;
import com.example.uade.tpo.FelsaniMotors.exceptions.CategoriaDuplicadaException;
import com.example.uade.tpo.FelsaniMotors.service.categoria.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

/*
 * Metodos CRUD
 * 
 * GET
 * 
 * - getAllCategorias = Devuelve todas las categorias
 * - getCategoriaById = Devuelve una categoria por su id
 * - getCategoriaByNombre = Devuelve una categoria por su nombre
 * 
 * POST
 * 
 * - createCategoria = Crea una nueva categoria
 * 
 * CRUD
 * 
 * - updateCategoria = Actualiza una categoria
 * 
 * DELETE
 * 
 * - deleteCategoria = Elimina una categoria
 * 
 */

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // --- Seccion GET --- //
    
    // GET - Obtengo todas las categorias en la db
    @GetMapping
    public ResponseEntity<Page<Categoria>> getAllCategorias(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null){

            return ResponseEntity.ok(categoriaService.getAllCategorias(PageRequest.of(0, Integer.MAX_VALUE)));
        }

        return ResponseEntity.ok(categoriaService.getAllCategorias(PageRequest.of(page, size)));
    }
    
    // GET - Obtengo una categoria con un id
    @GetMapping("/{idCategoria}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long idCategoria) {

        Optional<Categoria> result = categoriaService.getCategoriaById(idCategoria);

        if (result.isPresent()){

            return ResponseEntity.ok(result.get());
        }

        return ResponseEntity.noContent().build();
    }

    // GET - Obtengo una categoria por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categoria> getCategoriaByNombre(@PathVariable String nombre) {

        Optional<Categoria> result = categoriaService.findByNombre(nombre);

        if (result.isPresent()){

            return ResponseEntity.ok(result.get());
        }

        return ResponseEntity.noContent().build();
    }

    // --- Seccion POST --- //
    
    // POST - Crear una nueva categoría
    @PostMapping
    public ResponseEntity<Object> createCategoria(@RequestBody CategoriaRequest categoriaRequest) throws CategoriaDuplicadaException {

        Categoria result = categoriaService.createCategoria(
            categoriaRequest.getNombre(),
            categoriaRequest.getImagen(),
            categoriaRequest.getDescripcion()
        );

        return ResponseEntity.created(URI.create("/categorias/" + result.getIdCategoria())).body(result);
    }

    // --- Seccion PUT --- //
    
    // PUT - Actualizo categoria
    @PutMapping("/{idCategoria}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long idCategoria, @RequestBody CategoriaRequest categoriaRequest) {

        Categoria categoriaActualizada = categoriaService.updateCategoria(
            idCategoria,
            categoriaRequest.getNombre(),
            categoriaRequest.getImagen(),
            categoriaRequest.getDescripcion()
        );
        
        return ResponseEntity.ok(categoriaActualizada);
    }

    // --- Seccion DELETE --- //
    
    // DELETE - Elimino categoria
    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long idCategoria) {

        boolean eliminado = categoriaService.deleteCategoria(idCategoria);
        
        if (eliminado) {

            return ResponseEntity.noContent().build();
        } else {

            return ResponseEntity.notFound().build();
        }
    }
    
    
}
