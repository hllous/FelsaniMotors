package com.example.uade.tpo.FelsaniMotors.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AlmacenamientoService {

    private final Path raiz;
    private final String baseUrl;

    public AlmacenamientoService(
            @Value("${storage.root:src/main/resources/static/uploads}") String carpeta,
            @Value("${storage.base-url:/uploads/}") String baseUrl) throws IOException {
        this.raiz = Paths.get(carpeta);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        Files.createDirectories(this.raiz);
    }

    public String guardar(MultipartFile file) throws IOException {
        String ext = "";
        String nombre = file.getOriginalFilename();
        if (nombre != null && nombre.contains(".")) {
            ext = nombre.substring(nombre.lastIndexOf("."));
        }
        String archivo = UUID.randomUUID() + ext;
        Path destino = raiz.resolve(archivo).normalize();
        Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        return baseUrl + archivo; 
    }
}
