package com.example.uade.tpo.FelsaniMotors.service.categoria;

import com.example.uade.tpo.FelsaniMotors.entity.Categoria;
import com.example.uade.tpo.FelsaniMotors.exceptions.CategoriaDuplicadaException;
import com.example.uade.tpo.FelsaniMotors.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    // --- Seccion GET --- //
    
    @Override
    public Page<Categoria> getAllCategorias(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }
    
    @Override
    public Optional<Categoria> getCategoriaById(Long id) {
        return categoriaRepository.findById(id);
    }

    // --- Seccion POST --- //

    @Override
    public Categoria createCategoria(String nombre, String imagen, String descripcion) throws CategoriaDuplicadaException {

        Categoria categoriaExistente = categoriaRepository.findByNombre(nombre);
        if (categoriaExistente != null) {
            throw new CategoriaDuplicadaException();
        }
        
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(nombre);
        nuevaCategoria.setImagen(imagen);
        nuevaCategoria.setDescripcion(descripcion);
        
        return categoriaRepository.save(nuevaCategoria);
    }

    // --- Seccion PUT --- //
    
    @Override
    public Categoria updateCategoria(Long id, String nombre, String imagen, String descripcion) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);
        
        /* Veo si la categoria existe o no
         * 
         * - Si existe, actualizo sus datos
         * - Si no existe, creo una nueva
         * 
        */

        if (categoriaOpt.isPresent()) {
            // Si la categoría existe, actualizo sus propiedades
            Categoria categoria = categoriaOpt.get();
            categoria.setNombre(nombre);
            categoria.setImagen(imagen);
            categoria.setDescripcion(descripcion);
            return categoriaRepository.save(categoria);
        } else {
            // Si la categoría no existe, creo una nueva con los datos proporcionados
            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setNombre(nombre);
            nuevaCategoria.setImagen(imagen);
            nuevaCategoria.setDescripcion(descripcion);
            return categoriaRepository.save(nuevaCategoria);
        }
    }

    // --- Seccion DELETE --- //
    
    @Override
    public boolean deleteCategoria(Long id) {

        
        if (categoriaRepository.existsById(id)) {

            categoriaRepository.deleteById(id);
            return true; 
        }
        return false;
    }

    // --- Metodos auxiliares --- //


    @Override
    public Optional<Categoria> findByNombre(String nombre) {
        return Optional.ofNullable(categoriaRepository.findByNombre(nombre));
    }

}
