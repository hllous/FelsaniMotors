package com.example.uade.tpo.FelsaniMotors.service.auto;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.uade.tpo.FelsaniMotors.entity.Auto;
import com.example.uade.tpo.FelsaniMotors.exceptions.AutoDuplicateException;

public interface AutoService {
    Page<Auto> getAutos(Pageable pageable);
    Optional<Auto> getAutoById(Long autoId);
    Auto createAuto(Auto autoRequest) throws AutoDuplicateException;
    Page<Auto> getAutos(PageRequest pageRequest);
}
