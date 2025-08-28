package com.example.uade.tpo.FelsaniMotors.service.auto;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.exceptions.AutoDuplicateException;

public interface AutoService {
    Page<Auto> getAutos(PageRequest pageRequest);
    Optional<Auto> getAutoById(Long autoId);
    Auto createAuto(Auto autoRequest) throws AutoDuplicateException;
}
