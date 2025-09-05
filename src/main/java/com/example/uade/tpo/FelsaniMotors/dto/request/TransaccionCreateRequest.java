package com.example.uade.tpo.FelsaniMotors.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionCreateRequest {
    private Long idPublicacion;
    private Long idComprador;
    private float monto;
    private String metodoPago;
    private String referenciaPago;
    private String comentarios;
    
}
