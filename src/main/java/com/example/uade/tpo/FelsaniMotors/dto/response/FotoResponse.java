package com.example.uade.tpo.FelsaniMotors.dto.response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoResponse {
    private Long idFoto;
    private String base64Image;
    private Boolean esPrincipal;
    private Integer orden;
    
    public ResponseEntity<FotoResponse> toResponseEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        return new ResponseEntity<>(this, headers, HttpStatus.OK);
    }
}
