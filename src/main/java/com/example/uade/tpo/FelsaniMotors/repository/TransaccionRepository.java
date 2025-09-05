package com.example.uade.tpo.FelsaniMotors.repository;

import com.example.uade.tpo.FelsaniMotors.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    
    @Query("SELECT t FROM Transaccion t WHERE t.publicacion.idPublicacion = :idPublicacion")
    List<Transaccion> findByIdPublicacion(@Param("idPublicacion") Long idPublicacion);

}
