package com.example.uade.tpo.FelsaniMotors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosOpcionesResponse {
    private List<String> marcas;
    private List<String> modelos;
    private List<Integer> anios;
    private List<String> estados;
    private List<String> combustibles;
    private List<String> tipoCategorias;
    private List<String> tipoCajas;
    private List<String> motores;
}
