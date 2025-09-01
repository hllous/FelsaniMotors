package com.example.uade.tpo.FelsaniMotors.repository;

import com.example.uade.tpo.FelsaniMotors.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    @Query(value = "select c from Categoria c where c.tipoCategoria = ?1")
    Categoria findByTipoCategoria(String tipoCategoria);
}
