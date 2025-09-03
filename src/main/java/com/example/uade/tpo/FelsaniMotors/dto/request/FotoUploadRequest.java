package com.example.uade.tpo.FelsaniMotors.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoUploadRequest {
    private MultipartFile file;
    private Boolean esPrincipal;
    private Integer orden;
}
