package com.example.uade.tpo.FelsaniMotors.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFileRequest {
    private String name;
    private MultipartFile file;
}
