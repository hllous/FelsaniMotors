package com.example.uade.tpo.FelsaniMotors.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.uade.tpo.FelsaniMotors.entity.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Long> {
    Page<Foto> findByPublicacion_Id(Long publicacionId, Pageable pageable);
}
