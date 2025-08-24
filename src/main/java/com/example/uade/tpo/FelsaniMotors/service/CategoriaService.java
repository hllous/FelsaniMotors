package com.example.uade.tpo.FelsaniMotors.service;

import com.example.uade.tpo.FelsaniMotors.entity.Categoria;
import com.example.uade.tpo.FelsaniMotors.exceptions.CategoriaDuplicadaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CategoriaService {
    
    // Obtengo todas las categorias en la db
    Page<Categoria> getAllCategorias(Pageable pageable);
    
    // Obtengo una categoria con un id
    Optional<Categoria> getCategoriaById(Long id);
    
    // Creo una nueva categoria
    Categoria createCategoria(String nombre, String imagen, String descripcion) throws CategoriaDuplicadaException;
    
    // Actualizo una categoria
    Categoria updateCategoria(Long id, String nombre, String imagen, String descripcion);
    
    // Elimino categoria
    boolean deleteCategoria(Long id);

    // --- Metodos no implementados en controller --- //
    
    // Busco por nombre, utilizado en "getCategoriaByNombre()"
    Optional<Categoria> findByNombre(String nombre);
    
}
