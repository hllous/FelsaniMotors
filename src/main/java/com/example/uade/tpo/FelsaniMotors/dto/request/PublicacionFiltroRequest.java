package com.example.uade.tpo.FelsaniMotors.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionFiltroRequest {
    private List<String> marca;
    private List<String> modelo;
    private List<String> anio;
    private List<String> estado;
    private List<String> kilometraje;
    private List<String> combustible;
    private List<String> tipoCategoria;
    private List<String> tipoCaja;
    private List<String> motor;
}
