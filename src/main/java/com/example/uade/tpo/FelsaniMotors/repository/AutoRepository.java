package com.example.uade.tpo.FelsaniMotors.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;

@Repository

//Busca un auto por su marca y modelo
public interface AutoRepository extends JpaRepository<Auto, Long> {
    Optional<Auto> findByMarcaAndModelo(String marca, String modelo);
}