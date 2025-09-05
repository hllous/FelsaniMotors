package com.example.uade.tpo.FelsaniMotors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponse {
    private Long idTransaccion;
    private Long idPublicacion;
    private Long idComprador;
    private Long idVendedor;
    private float monto;
    private String metodoPago;
    private Date fechaTransaccion;
    private String estado;
    private String referenciaPago;
    private String comentarios;
    
    // Datos adicionales
    private String tituloPublicacion;
    private String nombreComprador;
    private String nombreVendedor;
}
