package com.example.uade.tpo.FelsaniMotors.service.auto;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.entity.Categoria;
import com.example.uade.tpo.FelsaniMotors.exceptions.AutoDuplicateException;
import com.example.uade.tpo.FelsaniMotors.repository.AutoRepository;
import com.example.uade.tpo.FelsaniMotors.repository.CategoriaRepository;

@Service
public class AutoServiceImpl implements AutoService {

    @Autowired
    private AutoRepository autoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Page<Auto> getAutos(PageRequest pageRequest) {
        return autoRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Auto> getAutoById(Long autoId) {
        return autoRepository.findById(autoId);
    }

    @Override
    public Auto createAuto(Auto autoRequest) throws AutoDuplicateException {

        Optional<Auto> existingAuto = autoRepository.findByMarcaAndModelo(
                autoRequest.getMarca(), autoRequest.getModelo());
        if (existingAuto.isPresent()) {
            throw new AutoDuplicateException("El auto ya existe.");
        }

        Auto auto = new Auto();
        auto.setMarca(autoRequest.getMarca());
        auto.setModelo(autoRequest.getModelo());
        auto.setAnio(autoRequest.getAnio());
        auto.setEstado(autoRequest.getEstado());
        auto.setKilometraje(autoRequest.getKilometraje());
        auto.setCombustible(autoRequest.getCombustible());
        auto.setCapacidadTanque(autoRequest.getCapacidadTanque());
        auto.setTipoCaja(autoRequest.getTipoCaja());
        auto.setMotor(autoRequest.getMotor());
        auto.setCategoria(autoRequest.getCategoria());
        return autoRepository.save(auto);
    }

    @Override
    public Auto asignarCategoria(Long idAuto, Long idCategoria) {
        // Buscar el auto
        Auto auto = autoRepository.findById(idAuto)
                .orElseThrow(() -> new RuntimeException("Auto no encontrado con ID: " + idAuto));
        
        // Buscar la categoria
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));
        
        // Asignar la categoria al auto
        auto.setCategoria(categoria);
        
        // Guardar y retornar el auto actualizado
        return autoRepository.save(auto);
    }
}