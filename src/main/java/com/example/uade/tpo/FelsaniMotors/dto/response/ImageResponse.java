package com.example.uade.tpo.FelsaniMotors.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta respuesta simplificada sigue el patrón exacto del ejemplo proporcionado.
 * Contiene únicamente el id y el contenido en base64 de la imagen.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private Long id;
    private String file;
}
