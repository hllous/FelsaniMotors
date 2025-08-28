package com.example.uade.tpo.FelsaniMotors.service.foto;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.uade.tpo.FelsaniMotors.entity.Foto;
import com.example.uade.tpo.FelsaniMotors.entity.Publicacion;

public interface FotoService {
    /**
     * Obtiene una página de fotos para una publicación
     */
    Page<Foto> getFotosByPublicacion(Long idPublicacion, Pageable pageable);
    
    /**
     * Obtiene una foto por su ID
     */
    Foto getFotoById(Long idFoto);
    
    /**
     * Obtiene los datos binarios de una foto
     */
    byte[] getFotoData(Long idFoto);
    
    /**
     * Agrega una foto a una publicación
     */
    Foto addFoto(Long idPublicacion, MultipartFile archivo, Boolean esPrincipal, Integer orden) throws IOException;
    
    /**
     * Elimina una foto
     */
    void deleteFoto(Long idFoto);
    
    /**
     * Marca una foto como principal para una publicación
     */
    Foto setMainFoto(Long fotoId);
    
    /**
     * Obtiene la foto principal de una publicación
     */
    Foto getMainFoto(Publicacion publicacion);
    
    /**
     * Actualiza el orden de una foto
     */
    Foto updateFotoOrder(Long idFoto, Integer orden);
}
