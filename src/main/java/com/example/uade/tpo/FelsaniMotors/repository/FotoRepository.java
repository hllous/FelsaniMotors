package com.example.uade.tpo.FelsaniMotors.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;

import java.util.Optional;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    Page<Foto> findByIdPublicacion(Long idPublicacion, Pageable pageable);
    
    /**
     * Encuentra la foto principal de una publicación
     */
    Optional<Foto> findByPublicacionAndEsPrincipal(Publicacion publicacion, Boolean esPrincipal);
}
