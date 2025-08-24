package com.example.uade.tpo.FelsaniMotors.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Foto;

public interface FotoService {
    Page<Foto> getFotosDePublicacion(Long publicacionId, Pageable pageable);
    Optional<Foto> getFotoById(Long id);
    Foto agregarFoto(Long publicacionId, MultipartFile file, Boolean esPrincipal, Integer orden) throws IOException;
    void eliminarFoto(Long fotoId);
}
