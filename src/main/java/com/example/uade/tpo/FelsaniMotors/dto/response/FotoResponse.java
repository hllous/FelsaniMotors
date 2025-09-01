package com.example.uade.tpo.FelsaniMotors.dto.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FotoResponse {
    private byte[] datos;
    private MediaType mediaType;
    
    public ResponseEntity<byte[]> toResponseEntity() {
        
        HttpHeaders headers = new HttpHeaders();

        if (mediaType != null) {
            headers.setContentType(mediaType);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        
        return new ResponseEntity<>(datos, headers, HttpStatus.OK);
    }
}
