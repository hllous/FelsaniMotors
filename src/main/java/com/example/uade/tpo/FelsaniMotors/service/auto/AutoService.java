package com.example.uade.tpo.FelsaniMotors.service.auto;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.exceptions.AutoDuplicateException;

public interface AutoService {

    //Obtener una lista de autos.
    Page<Auto> getAutos(PageRequest pageRequest);

    //Obtener un auto por su ID.
    Optional<Auto> getAutoById(Long autoId);

    //Crear un nuevo auto.
    Auto createAuto(Auto autoRequest) throws AutoDuplicateException;
    
    //Asignar un auto a una categoría
    Auto asignarCategoria(Long idAuto, Long idCategoria);
}
